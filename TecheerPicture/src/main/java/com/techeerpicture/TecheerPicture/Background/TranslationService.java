package com.techeerpicture.TecheerPicture.Background;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TranslationService {
    @Value("${GPT_API}")
    private String openAiApiKey;

    private static final Logger logger = LoggerFactory.getLogger(TranslationService.class);
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String translateToEnglish(String koreanPrompt) {
        try {
            OkHttpClient client = new OkHttpClient();
            ObjectMapper objectMapper = new ObjectMapper();

            logger.info("🔍 번역 요청: {}", koreanPrompt);

            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("model", "gpt-3.5-turbo");
            requestMap.put("temperature", 0.3); // 창의성 낮추고 정확도 높임
            requestMap.put("top_p", 0.9); // 신뢰도 높은 단어 선택

            List<Map<String, String>> messages = Arrays.asList(
                    new HashMap<String, String>() {{
                        put("role", "system");
                        put("content", "You are an AI specializing in image generation prompts. Convert the following Korean text into a high-quality English prompt for AI-generated background images. The output should be concise, detailed, and optimized for generating beautiful visuals. Avoid generic words and focus on vivid descriptions.\n\n"
                                + "Korean text: " + koreanPrompt + "\n\n"
                                + "Optimized AI image generation prompt:");
                    }}
            );

            requestMap.put("messages", messages);

            String requestBody = objectMapper.writeValueAsString(requestMap);

            Request request = new Request.Builder()
                    .url(OPENAI_URL)
                    .addHeader("Authorization", "Bearer " + openAiApiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to call OpenAI API: " + response.body().string());
            }

            JsonNode jsonResponse = objectMapper.readTree(response.body().string());
            String translatedPrompt = jsonResponse.get("choices").get(0).get("message").get("content").asText();

            logger.info("✅ 최적화된 프롬프트: {}", translatedPrompt);

            return translatedPrompt;
        } catch (Exception e) {
            throw new RuntimeException("Error translating prompt", e);
        }
    }
}