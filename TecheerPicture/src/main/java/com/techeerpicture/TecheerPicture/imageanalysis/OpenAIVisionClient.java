package com.techeerpicture.TecheerPicture.imageanalysis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class OpenAIVisionClient {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${GPT_API}")
    private String apiKey;

    public String analyzeImage(String imageUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "Describe the objects in the image and provide details about the background. " +
                                        "Include what kind of environment it is in."),
                        Map.of("role", "user", "content", List.of(
                                Map.of("type", "image_url", "image_url", Map.of("url", imageUrl))
                        ))
                ),
                "max_tokens", 100
        );

        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(objectMapper.writeValueAsString(requestBody), MediaType.get("application/json")))
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("OpenAI API 호출 실패: " + response.body().string());
        }

        JsonNode jsonResponse = objectMapper.readTree(response.body().string());
        JsonNode choices = jsonResponse.get("choices");

        if (choices == null || choices.isEmpty()) {
            throw new IOException("OpenAI Vision API 응답오류.");
        }

        return choices.get(0).get("message").get("content").asText();
    }
}
