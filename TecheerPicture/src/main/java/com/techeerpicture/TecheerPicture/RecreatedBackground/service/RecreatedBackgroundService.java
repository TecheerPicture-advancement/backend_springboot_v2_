package com.techeerpicture.TecheerPicture.RecreatedBackground.service;

import com.techeerpicture.TecheerPicture.Background.repository.BackgroundRepository;
import com.techeerpicture.TecheerPicture.Background.entity.Background;
import com.techeerpicture.TecheerPicture.Background.service.PixelcutService;
import com.techeerpicture.TecheerPicture.Background.dto.PixelcutRequest;
import com.techeerpicture.TecheerPicture.Background.entity.ImageTransform;
import com.techeerpicture.TecheerPicture.Image.entity.Image;
import com.techeerpicture.TecheerPicture.Image.repository.ImageRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecreatedBackgroundService {

  private final BackgroundRepository backgroundRepository;
  private final ImageRepository imageRepository;
  private final PixelcutService pixelcutService;

  @Autowired
  public RecreatedBackgroundService(BackgroundRepository backgroundRepository, ImageRepository imageRepository,
                                    PixelcutService pixelcutService) {
    this.backgroundRepository = backgroundRepository;
    this.imageRepository = imageRepository;
    this.pixelcutService = pixelcutService;
  }

  public Background recreateBackground(Long backgroundId) {
    // ✅ 기존 Background 데이터 가져오기
    Background originalBackground = backgroundRepository.findById(backgroundId)
        .orElseThrow(() -> new IllegalArgumentException("해당 ID의 Background를 찾을 수 없습니다: " + backgroundId));

    // ✅ Image ID를 이용해 원본 이미지 URL 가져오기
    String originalImageUrl = imageRepository.findById(originalBackground.getImageId())
        .map(Image::getImageUrl)
        .orElseThrow(() -> new IllegalArgumentException("해당 ID의 Image를 찾을 수 없습니다: " + originalBackground.getImageId()));

    // ✅ Pixelcut API 요청 데이터 생성
    PixelcutRequest pixelcutRequest = new PixelcutRequest(
        originalBackground.getImageId(),
        originalImageUrl,
        new ImageTransform(originalBackground.getScale(), originalBackground.getXCenter(), originalBackground.getYCenter()),
        originalBackground.getScene(),
        originalBackground.getPrompt(),
        null
    );

    // ✅ Pixelcut API 호출 → 새로운 배경 이미지 생성
    String apiResponse = pixelcutService.callPixelcutAPI(pixelcutRequest);
    String recreatedImageUrl = extractImageUrlFromResponse(apiResponse);

    // ✅ 새로운 Background 데이터 생성 (새로운 행 추가)
    Background newBackground = new Background();
    newBackground.setImageId(originalBackground.getImageId());
    newBackground.setImageUrl(recreatedImageUrl);
    newBackground.setScene(originalBackground.getScene());
    newBackground.setPrompt(originalBackground.getPrompt());
    newBackground.setIsRecreated(originalBackground.getId());  // ✅ 원본 ID 저장
    newBackground.setScale(originalBackground.getScale());
    newBackground.setXCenter(originalBackground.getXCenter());
    newBackground.setYCenter(originalBackground.getYCenter());

    return backgroundRepository.save(newBackground);  // ✅ 새로운 배경 데이터 저장
  }

  private String extractImageUrlFromResponse(String responseBody) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode responseJson = objectMapper.readTree(responseBody);
      JsonNode imageUrlNode = responseJson.get("result_url");

      if (imageUrlNode == null) {
        throw new RuntimeException("Pixelcut API 응답에서 'result_url'을 찾을 수 없습니다: " + responseBody);
      }

      return imageUrlNode.asText();
    } catch (Exception e) {
      throw new RuntimeException("Pixelcut API 응답을 파싱하는 중 오류 발생: " + responseBody, e);
    }
  }
}
