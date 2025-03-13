package com.techeerpicture.TecheerPicture.ImageToVideo;

import ai.fal.client.*;
import ai.fal.client.queue.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.JsonObject;
import com.techeerpicture.TecheerPicture.Image.entity.Image;
import com.techeerpicture.TecheerPicture.Image.repository.ImageRepository;
import com.techeerpicture.TecheerPicture.Background.service.TranslationService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Map;
import java.util.UUID;
import java.time.Instant;
import java.util.HashMap;

@Service
public class ImageToVideoService {
    private final ImageRepository imageRepository;
    private final ImageToVideoRepository imageToVideoRepository;
    private final FalClient falClient;
    private final AmazonS3 amazonS3;
    private final TranslationService translationService;
    private static final Logger logger = LoggerFactory.getLogger(ImageToVideoService.class);

    private final String bucketName = "techeer-picture-bucket";

    @Autowired
    public ImageToVideoService(ImageRepository imageRepository, ImageToVideoRepository imageToVideoRepository,
                               FalClient falClient, AmazonS3 amazonS3, TranslationService translationService) {
        this.imageRepository = imageRepository;
        this.imageToVideoRepository = imageToVideoRepository;
        this.falClient = falClient;
        this.amazonS3 = amazonS3;
        this.translationService = translationService;
    }

    public ImageToVideoResponse submitVideoJob(ImageToVideoRequest request) {
        try {
            Optional<Image> imageOptional = imageRepository.findById(request.getImageId());
            if (!imageOptional.isPresent()) {
                throw new RuntimeException("해당 imageId에 해당하는 이미지가 없습니다.");
            }
            Image image = imageOptional.get();
            String imageUrl = image.getImageUrl();

            String translatedPrompt = translationService.translateToEnglish(request.getPrompt());

            int maxLength = 250;
            if (translatedPrompt.length() > maxLength) {
                translatedPrompt = translatedPrompt.substring(0, maxLength);
            }

            String optimizedPrompt = translatedPrompt + " This is a cinematic, smoothly edited, high-quality 4K video with stunning visuals.";

            logger.info("📝 원본 프롬프트: {}", request.getPrompt());
            logger.info("✅ 번역된 프롬프트: {}", translatedPrompt);
            logger.info("🎬 최적화된 비디오 프롬프트: {}", optimizedPrompt);

            Map<String, Object> input = new HashMap<>();
            input.put("prompt", optimizedPrompt);
            input.put("image_url", imageUrl);

            if (request.getAspectRatio() != null) {
                input.put("aspect_ratio", request.getAspectRatio());
            }

            logger.info("🎥 Fal API 요청 데이터: {}", input);

            Output<JsonObject> result = falClient.subscribe(
                    "fal-ai/luma-dream-machine/ray-2/image-to-video",
                    SubscribeOptions.<JsonObject>builder()
                            .input(input)
                            .logs(true)
                            .resultType(JsonObject.class)
                            .build()
            );

            Thread.sleep(5000);

            if (result == null || result.getData() == null || !result.getData().has("video") || !result.getData().getAsJsonObject("video").has("url")) {
                logger.error("FalClient 응답에서 'video.url' 필드를 찾을 수 없습니다.");
                throw new RuntimeException("FalClient 응답에서 'video.url' 필드를 찾을 수 없습니다.");
            }

            String videoUrl = result.getData().getAsJsonObject("video").get("url").getAsString();
            logger.info("🎬 영상 URL: {}", videoUrl);

            String s3Url = uploadVideoToS3(videoUrl);
            logger.info("✅ S3 업로드 완료: {}", s3Url);

            ImageToVideo imageToVideo = new ImageToVideo();
            imageToVideo.setImage(image);
            imageToVideo.setPrompt(translatedPrompt);
            imageToVideo.setVideoUrl(videoUrl);
            imageToVideoRepository.save(imageToVideo);

            return new ImageToVideoResponse(new ImageToVideoResponse.VideoResponse(s3Url, null, null, 0), optimizedPrompt);

        } catch (Exception e) {
            logger.error("❌ 영상 생성 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("FalClient 요청 실패", e);
        }
    }

    public String uploadVideoToS3(String videoUrl) {  // private → public
        try {
            URL url = new URL(videoUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            String uniqueFileName = "video-" + UUID.randomUUID() + "-" + Instant.now().toEpochMilli() + ".mp4";
            amazonS3.putObject(new PutObjectRequest(bucketName, uniqueFileName, inputStream, null));

            return amazonS3.getUrl(bucketName, uniqueFileName).toString();
        } catch (Exception e) {
            throw new RuntimeException("S3 업로드 중 오류 발생: " + e.getMessage(), e);
        }
    }

}
