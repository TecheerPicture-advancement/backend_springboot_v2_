package com.techeerpicture.TecheerPicture.ImageToVideo;

import ai.fal.client.*;
import ai.fal.client.queue.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.JsonObject;
import com.techeerpicture.TecheerPicture.Image.Image;
import com.techeerpicture.TecheerPicture.Image.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.HttpURLConnection;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Map;
import java.util.UUID;
import java.time.Instant;

@Service
public class ImageToVideoService {
    private final ImageRepository imageRepository;
    private final ImageToVideoRepository imageToVideoRepository;
    private final FalClient falClient;
    private final AmazonS3 amazonS3;
    private static final Logger logger = LoggerFactory.getLogger(ImageToVideoService.class);

    private final String bucketName = "techeer-picture-bucket";

    public ImageToVideoService(ImageRepository imageRepository, ImageToVideoRepository imageToVideoRepository, FalClient falClient, AmazonS3 amazonS3) {
        this.imageRepository = imageRepository;
        this.imageToVideoRepository = imageToVideoRepository;
        this.falClient = falClient;
        this.amazonS3 = amazonS3;

    }

    public ImageToVideoResponse submitVideoJob(ImageToVideoRequest request) {
        try {
            // `imageId`로 `imageUrl` 조회
            Optional<Image> imageOptional = imageRepository.findById(request.getImageId());
            if (!imageOptional.isPresent()) {
                throw new RuntimeException("해당 imageId에 해당하는 이미지가 없습니다.");
            }
            Image image = imageOptional.get();
            String imageUrl = image.getImageUrl();

            // 클라이언트 프롬프트 유지 + 서버 프롬프트 추가
            String finalPrompt = request.getPrompt() + enhanceProductPrompt();
            logger.info("Fal API 요청 데이터: image_url={}, prompt={}", imageUrl, finalPrompt);

            // `input` 데이터 구성
            Map<String, Object> input = Map.of(
                    "image_url", imageUrl,
                    "prompt", finalPrompt
            );

            logger.info("Fal API 요청 데이터: {}", input);

            // `result` 변수를 `try` 블록 바깥에서 선언
            Output<JsonObject> result = null;

            try {
                result = falClient.subscribe(
                        "fal-ai/luma-dream-machine/image-to-video",
                        SubscribeOptions.<JsonObject>builder()
                                .input(input)
                                .logs(true)
                                .resultType(JsonObject.class)
                                .onQueueUpdate(update -> {
                                    if (update instanceof QueueStatus.InProgress) {
                                        logger.info("Queue Progress: {}", ((QueueStatus.InProgress) update).getLogs());
                                    }
                                })
                                .build()
                );

                Thread.sleep(5000);

            } catch (Exception e) {
                logger.error("영상 생성 중 오류 발생: {}", e.getMessage(), e);
                throw new RuntimeException("FalClient 요청 실패", e);
            }

            // 🔥 `result` 변수가 `try` 블록을 벗어나도 접근 가능하도록 변경
            if (result == null || result.getData() == null || !result.getData().has("video") || !result.getData().getAsJsonObject("video").has("url")) {
                logger.error("FalClient 응답에서 'video.url' 필드를 찾을 수 없습니다.");
                throw new RuntimeException("FalClient 응답에서 'video.url' 필드를 찾을 수 없습니다.");
            }

            String videoUrl = result.getData().getAsJsonObject("video").get("url").getAsString();
            logger.info("영상 URL: {}", videoUrl);

            // ✅ S3에 업로드 및 URL 저장
            String s3Url = uploadVideoToS3(videoUrl);
            logger.info("S3 업로드 완료: {}", s3Url);

            return new ImageToVideoResponse(new ImageToVideoResponse.VideoResponse(s3Url, null, null, 0), finalPrompt);

        } catch (Exception e) {
            logger.error("영상 생성 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("FalClient 요청 실패", e);
        }
    }

    // ✅ `uploadVideoToS3` 메서드가 `submitVideoJob` 밖에 있어야 함!
    public String uploadVideoToS3(String videoUrl) {
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

    /**
     * 광고 최적화 프롬프트 자동 추가
     */
    private String enhanceProductPrompt() {
        return ", studio lighting, vibrant colors, product showcase, 4K, high contrast,wide shot, full composition, balanced framing";
    }
}
