package com.techeerpicture.TecheerPicture.Banner.external;

import com.techeerpicture.TecheerPicture.Banner.util.GeneratedTexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class GPTService {

  @Value("${GPT_API}")
  private String apiKey;

  @Async("gptExecutor")
  public CompletableFuture<GeneratedTexts> generateAdTexts(
      String itemName, String itemConcept, String itemCategory,
      String addInformation, String imageUrl
  ) {
    return CompletableFuture.completedFuture(
        generateAdTextsSync(itemName, itemConcept, itemCategory, addInformation, imageUrl)
    );
  }

  public GeneratedTexts generateAdTextsSync(String itemName, String itemConcept, String itemCategory, String addInformation, String imageUrl) {
    String apiUrl = "https://api.openai.com/v1/chat/completions";

    String prompt = String.format(
        "다음 제품 정보를 바탕으로 광고 문구를 생성해주세요:\n" +
            "제품 이름: '%s'\n" +
            "컨셉: '%s'\n" +
            "카테고리: '%s'\n" +
            "추가 정보: '%s'\n\n" +
            "이 이미지url 을 분석해주세요. : '%s'\n\n" +
            "이미지 분위기를 제품 정보에 추가적으로 반영하여 광고 문구를 생성하세요.\n" +
            "각 광고는 메인 문장(maintext)과 서브 문장(servtext)로 구성됩니다.\n" +
            "출력 형식:\n" +
            "- 세트 1:\n" +
            "  메인 문장: [maintext1]\n" +
            "  서브 문장: [servtext1]\n" +
            "- 세트 2:\n" +
            "  메인 문장: [maintext2]\n" +
            "  서브 문장: [servtext2]",
        itemName, itemConcept, itemCategory, addInformation, imageUrl
    );

    Map<String, Object> requestBody = Map.of(
        "model", "gpt-3.5-turbo",
        "messages", List.of(
            Map.of("role", "system", "content", "You are a helpful assistant."),
            Map.of("role", "user", "content", prompt)
        ),
        "max_tokens", 200,
        "temperature", 0.7
    );

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + apiKey);
    headers.set("Content-Type", "application/json");

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
    RestTemplate restTemplate = new RestTemplate();

    try {
      ResponseEntity<Map> responseEntity = restTemplate.postForEntity(apiUrl, entity, Map.class);
      Map<String, Object> responseBody = responseEntity.getBody();

      if (responseBody == null || !responseBody.containsKey("choices")) {
        throw new RuntimeException("OpenAI 응답이 올바르지 않습니다.");
      }

      List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
      Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
      String generatedText = (String) message.get("content");

      if (generatedText == null || generatedText.isEmpty()) {
        throw new RuntimeException("OpenAI로부터 생성된 텍스트가 비어 있습니다.");
      }

      return parseGeneratedText(generatedText);
    } catch (Exception e) {
      throw new RuntimeException("GPT 호출 실패: " + e.getMessage(), e);
    }
  }

  private GeneratedTexts parseGeneratedText(String generatedText) {
    String[] lines = generatedText.split("\n");

    String mainText1 = removeQuotes(findText(lines, "- 세트 1:", "메인 문장:", "서브 문장:"));
    String servText1 = removeQuotes(findText(lines, "- 세트 1:", "서브 문장:", "- 세트 2:"));
    String mainText2 = removeQuotes(findText(lines, "- 세트 2:", "메인 문장:", "서브 문장:"));
    String servText2 = removeQuotes(findText(lines, "- 세트 2:", "서브 문장:", null));

    return new GeneratedTexts(mainText1, servText1, mainText2, servText2);
  }

  private String findText(String[] lines, String setMarker, String startMarker, String endMarker) {
    boolean isInSet = false;
    for (String line : lines) {
      if (setMarker != null && line.contains(setMarker)) isInSet = true;

      if (isInSet && line.contains(startMarker)) {
        int startIndex = line.indexOf(startMarker) + startMarker.length();
        int endIndex = (endMarker != null && line.contains(endMarker))
            ? line.indexOf(endMarker, startIndex)
            : line.length();
        return line.substring(startIndex, endIndex).trim();
      }
    }
    return "";
  }

  private String removeQuotes(String text) {
    return text == null ? "" : text.replaceAll("^\"|\"$", "");
  }
}
