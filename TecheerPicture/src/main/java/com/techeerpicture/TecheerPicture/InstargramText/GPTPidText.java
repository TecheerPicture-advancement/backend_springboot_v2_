package com.techeerpicture.TecheerPicture.instagramtext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GPTPidText {

    @Value("${GPT_API}")
    private String apiKey;

    public String generateInstagramText(String textPrompt) {
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        String prompt = String.format(
                "다음 설명을 바탕으로 Instagram 광고 문장을 작성해주세요:\n'%s'\n\n" +
                        "문장은 짧고 강렬해야 하며, 해시태그를 포함해야 합니다.\n" +
                        "예시: '🔥 단 3일간만! 맥북 프로 할인 행사! 지금 바로 득템하세요 💻✨ #맥북프로 #한정특가 #할인행사'",
                textPrompt
        );

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a social media marketing expert."),
                        Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", 100,
                "temperature", 0.8
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(apiUrl, entity, Map.class);
        Map<String, Object> responseBody = responseEntity.getBody();

        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return (String) message.get("content");
    }
}
