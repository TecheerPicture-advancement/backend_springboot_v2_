package com.techeerpicture.TecheerPicture.Banner.external;

import org.springframework.scheduling.annotation.Async;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.techeerpicture.TecheerPicture.Banner.util.GeneratedTexts;
/**
 * GPTService
 *
 * OpenAI GPT API를 사용하여 광고 문구를 생성하는 서비스 클래스입니다.
 * 입력된 제품 정보를 바탕으로 GPT를 호출하여 광고 문구를 생성하고, 이를 처리하여 반환합니다.
 */
@Service
public class GPTService {

  @Value("${GPT_API}")
  private String apiKey; // OpenAI API 키를 저장하는 필드

  /**
   * 광고 문구를 생성하는 메서드
   *
   * @param itemName       제품 이름
   * @param itemConcept    제품 컨셉
   * @param itemCategory   제품 카테고리
   * @param addInformation 추가 정보
   * @return GeneratedTexts 생성된 광고 문구를 담은 객체
   */
  public GeneratedTexts generateAdTexts(String itemName, String itemConcept, String itemCategory, String addInformation, String imageUrl) {
    String apiUrl = "https://api.openai.com/v1/chat/completions";

    // GPT 요청 프롬프트 작성
    String prompt = String.format(
        "다음 제품 정보를 바탕으로 광고 문구를 생성해주세요:\n" +
            "제품 이름: '%s'\n" +
            "컨셉: '%s'\n" +
            "카테고리: '%s'\n" +
            "추가 정보: '%s'\n\n" +
            "이 이미지url 을 분석해주세요. : '%s'\n\n" +
            "이미지 분위기를 제품 정보에 추가적으로  반영하여 광고 문구를 생성하세요.\n" +
            "각 광고는 메인 문장(maintext)과 서브 문장(servtext)로 구성됩니다.\n" +
            "출력 형식:\n" +
            "- 세트 1:\n" +
            "  메인 문장: [maintext1]\n" +
            "  서브 문장: [servtext1]\n" +
            "- 세트 2:\n" +
            "  메인 문장: [maintext2]\n" +
            "  서브 문장: [servtext2]",
        itemName, itemConcept, itemCategory, addInformation,imageUrl
    );

    // 요청 본문 생성
    Map<String, Object> requestBody = Map.of(
        "model", "gpt-4o",
        "messages", List.of(
            Map.of("role", "system", "content", "You are a helpful assistant."),
            Map.of("role", "user", "content", prompt)
        ),
        "max_tokens", 200,
        "temperature", 0.7
    );

    // 요청 헤더 설정
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + apiKey);
    headers.set("Content-Type", "application/json");

    // 요청 엔티티 생성
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
    RestTemplate restTemplate = new RestTemplate();

    try {
      // OpenAI API 호출
      ResponseEntity<Map> responseEntity = restTemplate.postForEntity(apiUrl, entity, Map.class);
      Map<String, Object> responseBody = responseEntity.getBody();

      // 응답 검증
      if (responseBody == null || !responseBody.containsKey("choices")) {
        throw new RuntimeException("OpenAI 응답이 올바르지 않습니다.");
      }

      // 응답 데이터 추출
      List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
      Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
      String generatedText = (String) message.get("content");

      // 생성된 텍스트 검증
      if (generatedText == null || generatedText.isEmpty()) {
        throw new RuntimeException("OpenAI로부터 생성된 텍스트가 비어 있습니다.");
      }

      // 생성된 텍스트 파싱
      return parseGeneratedText(generatedText);

    } catch (Exception e) {
      throw new RuntimeException("GPT 호출 실패: " + e.getMessage(), e);
    }
  }
  @Async
  public CompletableFuture<GeneratedTexts> generateAdTextsAsync(
      String itemName, String itemConcept, String itemCategory,
      String addInformation, String imageUrl
  ) {
    GeneratedTexts texts = generateAdTexts(itemName, itemConcept, itemCategory, addInformation, imageUrl);
    return CompletableFuture.completedFuture(texts);
  }
  /**
   * 생성된 텍스트를 파싱하여 광고 문구를 추출하는 메서드
   *
   * @param generatedText 생성된 GPT 응답 텍스트
   * @return GeneratedTexts 추출된 광고 문구를 담은 객체
   */
  private GeneratedTexts parseGeneratedText(String generatedText) {
    String[] lines = generatedText.split("\n");

    // 세트 1과 세트 2의 메인 및 서브 문장 추출
    String mainText1 = removeQuotes(findText(lines, "- 세트 1:", "메인 문장:", "서브 문장:"));
    String servText1 = removeQuotes(findText(lines, "- 세트 1:", "서브 문장:", "- 세트 2:"));
    String mainText2 = removeQuotes(findText(lines, "- 세트 2:", "메인 문장:", "서브 문장:"));
    String servText2 = removeQuotes(findText(lines, "- 세트 2:", "서브 문장:", null));

    return new GeneratedTexts(mainText1, servText1, mainText2, servText2);
  }

  /**
   * 특정 텍스트 블록에서 마커를 사용하여 텍스트를 추출하는 메서드
   *
   * @param lines      텍스트 라인 배열
   * @param setMarker  세트를 구분하는 마커
   * @param startMarker 시작 마커
   * @param endMarker   끝 마커 (null이면 라인 끝까지 추출)
   * @return 추출된 텍스트
   */
  private String findText(String[] lines, String setMarker, String startMarker, String endMarker) {
    boolean isInSet = false;

    for (String line : lines) {
      if (setMarker != null && line.contains(setMarker)) {
        isInSet = true; // 해당 세트에 진입
      }

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

  /**
   * 문자열의 양끝에 있는 따옴표를 제거하는 메서드
   *
   * @param text 입력 문자열
   * @return 따옴표가 제거된 문자열
   */
  private String removeQuotes(String text) {
    if (text == null) return "";
    return text.replaceAll("^\"|\"$", ""); // 앞뒤의 "를 제거
  }
}