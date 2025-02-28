package com.techeerpicture.TecheerPicture.RemoveBackground.service;

import com.techeerpicture.TecheerPicture.Background.Background;
import com.techeerpicture.TecheerPicture.Background.BackgroundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RemoveBackgroundService {

  private final BackgroundRepository backgroundRepository;

  @Value("${PIXELCUT_APIKEY}")
  private String apiKey;

  private static final String API_URL = "https://api.developer.pixelcut.ai/v1/remove-background";

  @Transactional
  public String removeBackground(Long imageId) {
    // ✅ 여러 개의 Background 중 첫 번째 데이터만 사용
    List<Background> backgrounds = backgroundRepository.findByImageId(imageId);
    if (backgrounds.isEmpty()) {
      throw new IllegalArgumentException("해당 imageId에 대한 Background가 없습니다.");
    }

    Background background = backgrounds.get(0); // ✅ 여러 개일 경우 첫 번째만 사용

    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");

    JSONObject jsonBody = new JSONObject();
    jsonBody.put("image_url", background.getImageUrl());
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
        String resultUrl = responseObject.getString("result_url");

        // ✅ 배경 정보 업데이트
        background.setImageUrl(resultUrl);
        background.setTypeToRemove();
        backgroundRepository.save(background);

        return resultUrl;
      } else {
        log.error("API 요청 실패: {}", response);
        throw new RuntimeException("Failed to remove background: " + response);
      }
    } catch (IOException e) {
      log.error("API 호출 중 오류 발생", e);
      throw new RuntimeException("Error calling remove background API", e);
    }
  }
}
