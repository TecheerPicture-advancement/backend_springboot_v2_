package com.techeerpicture.TecheerPicture.Background;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeerpicture.TecheerPicture.Background.BackgroundRequest;
import com.techeerpicture.TecheerPicture.Background.PixelcutRequest;
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


            MediaType JSON = MediaType.parse("application/json");
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonBody = objectMapper.writeValueAsString(pixelcutRequest);
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);

            // HTTP 요청 생성
            Request httpRequest = new Request.Builder()
                .url("https://api.developer.pixelcut.ai/v1/generate-background")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("X-API-KEY", apiKey)
                .post(requestBody)
                .build();


            // API 호출
            Response response = client.newCall(httpRequest).execute();


            if (!response.isSuccessful()) {
                String errorResponse = response.body() != null ? response.body().string() : "No response body";
                logger.error("Pixelcut API request failed: {} - {}", response.code(), errorResponse);
                throw new RuntimeException("Failed to call Pixelcut API: " + errorResponse);
            }

            String responseBody = response.body().string();
            logger.info("✅ Pixelcut API Response: {}", responseBody);

            return responseBody;
        } catch (Exception e) {
            logger.error("Error calling Pixelcut API: ", e);
            throw new RuntimeException("Error calling Pixelcut API", e);
        }
    }
}