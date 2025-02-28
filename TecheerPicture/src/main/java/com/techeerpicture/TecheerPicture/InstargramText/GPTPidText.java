package com.techeerpicture.TecheerPicture.instagramtext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

@Service
public class GPTPidText {
    @Value("${GPT_API}")
    private String apiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final Logger logger = LoggerFactory.getLogger(GPTPidText.class);

    public String analyzeImageAndGenerateText(String fullPrompt) {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        logger.info("OpenAI에 전달될 프롬프트: {}", fullPrompt);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "system", "content", "Generate an Instagram caption similar in style to the provided examples."),
                        Map.of("role", "user", "content", fullPrompt)
                ),
                "max_tokens", 100
        );

        try {
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            Request request = new Request.Builder()
                    .url(OPENAI_API_URL)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(jsonBody, okhttp3.MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("OpenAI API 호출 실패: " + response.body().string());
                }

                JsonNode jsonResponse = objectMapper.readTree(response.body().string());
                JsonNode choices = jsonResponse.get("choices");

                if (choices == null || choices.isEmpty()) {
                    throw new IOException("OpenAI 응답 오류.");
                }

                String result = choices.get(0).get("message").get("content").asText();

                logger.info("OpenAI가 생성한 문장: {}", result);

                return result;
            }
        } catch (IOException e) {
            throw new RuntimeException("Instagram 문장 생성 중 오류 발생", e);
        }
    }
}
