package com.techeerpicture.TecheerPicture.Backgrounds;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeerpicture.TecheerPicture.Backgrounds.BackgroundRequest;
import com.techeerpicture.TecheerPicture.Backgrounds.PixelcutRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PixelcutService {
    private static final Logger logger = LoggerFactory.getLogger(PixelcutService.class);

    @Value("${PIXELCUT_APIKEY}")
    private String apiKey;

    public String callPixelcutAPI(PixelcutRequest pixelcutRequest) {
        try {
            OkHttpClient client = new OkHttpClient();

            // ✅ 요청 데이터 로깅 (디버깅용)
            logger.info("🚀 Sending request to Pixelcut API:");
            logger.info("🔹 imageTransform: {}", pixelcutRequest.getImageTransform().toJson());
            logger.info("🔹 scene: {}", pixelcutRequest.getScene());
            logger.info("🔹 prompt: {}", pixelcutRequest.getPrompt());
            logger.info("🔹 negativePrompt: {}", pixelcutRequest.getNegativePrompt());
            logger.info("🔹 imageUrl: {}", pixelcutRequest.getImageUrl());

            // ✅ JSON 데이터로 요청을 보내야 하므로 MediaType을 application/json으로 설정
            MediaType JSON = MediaType.parse("application/json");
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonBody = objectMapper.writeValueAsString(pixelcutRequest);
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);

            // HTTP 요청 생성
            Request httpRequest = new Request.Builder()
                .url("https://api.developer.pixelcut.ai/v1/generate-background")
                .addHeader("Content-Type", "application/json") // ✅ JSON 요청 형식 명시
                .addHeader("Accept", "application/json")
                .addHeader("X-API-KEY", apiKey) // ✅ 올바른 인증 헤더 추가
                .post(requestBody)
                .build();

            logger.info("🛠️ Request Headers: {}", httpRequest.headers());
            logger.info("🛠️ Request Body: {}", jsonBody);

            // API 호출
            Response response = client.newCall(httpRequest).execute();

            // 응답 상태 코드 확인 ✅
            logger.info("🔍 Pixelcut API Response Code: {}", response.code());

            if (!response.isSuccessful()) {
                String errorResponse = response.body() != null ? response.body().string() : "No response body";
                logger.error("❌ Pixelcut API request failed: {} - {}", response.code(), errorResponse);
                throw new RuntimeException("Failed to call Pixelcut API: " + errorResponse);
            }

            String responseBody = response.body().string();
            logger.info("✅ Pixelcut API Response: {}", responseBody);

            return responseBody;
        } catch (Exception e) {
            logger.error("❌ Error calling Pixelcut API: ", e);
            throw new RuntimeException("Error calling Pixelcut API", e);
        }
    }
}