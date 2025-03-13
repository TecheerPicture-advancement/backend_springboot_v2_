package com.techeerpicture.TecheerPicture.RemoveBackground.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.techeerpicture.TecheerPicture.Background.entity.Background;
import com.techeerpicture.TecheerPicture.Background.repository.BackgroundRepository;
import com.techeerpicture.TecheerPicture.Image.entity.Image;
import com.techeerpicture.TecheerPicture.Image.repository.ImageRepository;

import com.techeerpicture.TecheerPicture.RemoveBackground.dto.RemoveBackgroundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RemoveBackgroundService {

  private final ImageRepository imageRepository;
  private final BackgroundRepository backgroundRepository;
  private final AmazonS3 amazonS3;

  private String bucketName = "techeer-picture-bucket";

  @Value("${PIXELCUT_APIKEY}")
  private String apiKey;

  private static final String API_URL = "https://api.developer.pixelcut.ai/v1/remove-background";

  @Transactional
  public RemoveBackgroundResponse removeBackground(Long imageId) {
    // 1. Image 엔티티에서 이미지 조회
    Image image = imageRepository.findById(imageId)
        .orElseThrow(() -> new IllegalArgumentException("해당 imageId에 대한 Image가 없습니다."));

    // 2. Pixelcut API 호출
    String resultUrl = callRemoveBackgroundApi(image.getImageUrl());

    // 3. S3에 이미지 업로드 (IOException 처리 추가)
    final String s3Url;
    try {
      s3Url = uploadImageToS3(resultUrl);
    } catch (IOException e) {
      log.error("S3 업로드 중 오류 발생", e);
      throw new RuntimeException("S3 업로드에 실패했습니다.", e);
    }

    // 4. Background 엔티티에 저장 (Optional.map 활용)
    Optional<Background> optionalBackground = backgroundRepository.findByImageId(imageId);
    Background background = optionalBackground
        .map(existingBackground -> {
          existingBackground.setImageUrl(s3Url);
          existingBackground.setTypeToRemove(); // ✅ "remove" 타입으로 설정
          return existingBackground;
        })
        .orElseGet(() -> {
          Background newBackground = new Background(imageId, s3Url);
          newBackground.setTypeToRemove(); // ✅ "remove" 타입으로 설정
          return newBackground;
        });

    backgroundRepository.save(background);

    // 5. 결과 반환
    return new RemoveBackgroundResponse(200, "배경 제거 성공", background.getId(), s3Url);
  }

  private String callRemoveBackgroundApi(String imageUrl) {
    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");

    JSONObject jsonBody = new JSONObject();
    jsonBody.put("image_url", imageUrl);
    jsonBody.put("format", "png");

    RequestBody body = RequestBody.create(mediaType, jsonBody.toString());
    Request request = new Request.Builder()
        .url(API_URL)
        .post(body)
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json")
        .addHeader("X-API-KEY", apiKey)
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful() && response.body() != null) {
        JSONObject responseObject = new JSONObject(response.body().string());
        return responseObject.getString("result_url");
      } else {
        log.error("API 요청 실패: {}", response);
        throw new RuntimeException("Failed to remove background: " + response);
      }
    } catch (IOException e) {
      log.error("API 호출 중 오류 발생", e);
      throw new RuntimeException("Error calling remove background API", e);
    }
  }

  private String uploadImageToS3(String imageUrl) throws IOException {
    String fileName = "backgrounds/" + UUID.randomUUID() + ".png";
    String filePath = "/tmp/" + fileName;

    // ✅ 디렉터리 생성 코드 추가
    File directory = new File("/tmp/backgrounds");
    if (!directory.exists()) {
      directory.mkdirs(); // 경로에 필요한 디렉터리를 모두 생성
    }

    // ✅ URL에서 파일 다운로드
    try (java.io.InputStream in = new URL(imageUrl).openStream()) {
      Files.copy(in, Paths.get(filePath));
    }

    // ✅ S3에 업로드
    File file = new File(filePath);
    amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));

    // ✅ S3 URL 반환
    return amazonS3.getUrl(bucketName, fileName).toString();
  }
}
