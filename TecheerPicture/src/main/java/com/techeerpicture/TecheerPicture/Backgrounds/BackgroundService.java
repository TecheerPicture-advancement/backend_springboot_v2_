package com.techeerpicture.TecheerPicture.Backgrounds;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeerpicture.TecheerPicture.Backgrounds.PixelcutService;
import com.techeerpicture.TecheerPicture.Backgrounds.Background;
import com.techeerpicture.TecheerPicture.Backgrounds.BackgroundRepository;
import com.techeerpicture.TecheerPicture.Image.Image;
import com.techeerpicture.TecheerPicture.Image.ImageRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Service
public class BackgroundService {

    private final ImageRepository imageRepository;
    private final PixelcutService pixelcutService;
    private final BackgroundRepository backgroundRepository;
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(BackgroundService.class);
    private final AmazonS3 amazonS3;
    private final String bucketName = "techeer-picture-bucket";

    @Autowired
    public BackgroundService(ImageRepository imageRepository, PixelcutService pixelcutService,
                             BackgroundRepository backgroundRepository, AmazonS3 amazonS3, JdbcTemplate jdbcTemplate) {
        this.imageRepository = imageRepository;
        this.pixelcutService = pixelcutService;
        this.backgroundRepository = backgroundRepository;
        this.amazonS3 = amazonS3;
        this.jdbcTemplate = jdbcTemplate;
    }

    // imageId를 기반으로 URL 가져오기
    public String getImageUrl(Long imageId) {
        String sql = "SELECT url FROM images WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{imageId}, String.class);
    }

    /**
     * 사용자 요청을 처리하여 배경을 생성하고 저장하는 메서드.
     * @param backgroundRequest 사용자 요청 데이터.
     * @return 저장된 Background 엔티티.
     */
    public Background createAndSaveBackground(BackgroundRequest backgroundRequest) {
        try {
            // Retrieve image URL
            String imageUrl = imageRepository.findById(backgroundRequest.getImageId())
                .map(Image::getImageUrl)
                .orElseThrow(() -> new IllegalArgumentException("Invalid imageId: " + backgroundRequest.getImageId()));

            // Create PixelcutRequest
            PixelcutRequest pixelcutRequest = new PixelcutRequest(
                backgroundRequest.getImageId(),
                imageUrl,
                backgroundRequest.getImageTransform(),
                backgroundRequest.getScene(),
                backgroundRequest.getPrompt(),
                null // Negative prompt
            );
            logger.info("ImageTransform as JSON: {}", pixelcutRequest.getImageTransform().toJson());

            String apiResponse = pixelcutService.callPixelcutAPI(pixelcutRequest);
            String generatedImageUrl = extractImageUrlFromResponse(apiResponse); // Pixelcut API에서 반환된 이미지 URL

            //Pixelcut API에서 받은 이미지 S3 업로드
            String s3ImageUrl = uploadImageToS3(generatedImageUrl);

            //Background 엔티티 저장
            Background background = new Background();
            background.setImageId(backgroundRequest.getImageId());
            background.setScale(backgroundRequest.getImageTransform().getScale());
            background.setXCenter(backgroundRequest.getImageTransform().getXCenter());
            background.setYCenter(backgroundRequest.getImageTransform().getYCenter());
            background.setImageUrl(s3ImageUrl); //S3 업로드된 URL 저장
            background.setScene(backgroundRequest.getScene());
            background.setPrompt(backgroundRequest.getPrompt());

            return backgroundRepository.save(background); //DB 저장

        } catch (Exception e) {
            throw new RuntimeException("Error generating background", e);
        }
    }

    /**
     * Pixelcut API 응답에서 image_url을 추출하는 메서드
     */
    private String extractImageUrlFromResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(responseBody);

            // ✅ "result_url" 필드에서 값 가져오기 (Pixelcut API 응답에 맞게 수정)
            JsonNode imageUrlNode = responseJson.get("result_url");

            if (imageUrlNode == null) {
                throw new RuntimeException("Pixelcut API 응답에 'result_url' 필드가 없습니다: " + responseBody);
            }

            return imageUrlNode.asText(); // ✅ 올바르게 URL 반환
        } catch (Exception e) {
            throw new RuntimeException("Pixelcut API 응답을 파싱하는 중 오류 발생: " + responseBody, e);
        }
    }


    /**
     * URL에서 이미지를 다운로드한 후 S3에 업로드하고 URL을 반환하는 메서드
     */
    private String uploadImageToS3(String imageUrl) {
        try {
            // ✅ 1. URL에서 이미지 다운로드
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();

            // ✅ 2. S3에 업로드할 파일 이름 생성
            String fileName = "backgrounds/" + UUID.randomUUID() + ".jpg";

            // ✅ 3. S3에 업로드
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");
            amazonS3.putObject(bucketName, fileName, inputStream, metadata);

            // ✅ 4. 업로드된 S3 이미지 URL 반환
            return amazonS3.getUrl(bucketName, fileName).toString();

        } catch (Exception e) {
            throw new RuntimeException("Error uploading image to S3", e);
        }
    }

    /**
     * 특정 배경 데이터를 ID로 조회.
     * @param id 배경 ID.
     * @return 조회된 Background 엔티티(Optional).
     */
    public Background getBackgroundById(Long id) {
        return backgroundRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 Background를 찾을 수 없습니다: " + id));
    }
    /**
     * 특정 배경 데이터를 삭제.
     * @param id 삭제할 배경 ID.
     */
    public void deleteBackground(Long id) {
        Background background = backgroundRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 Background를 찾을 수 없습니다: " + id));

        background.setIsDeleted(true); // ✅ isDeleted 필드를 true로 변경
        backgroundRepository.save(background); // ✅ 변경 사항 저장
    }


}