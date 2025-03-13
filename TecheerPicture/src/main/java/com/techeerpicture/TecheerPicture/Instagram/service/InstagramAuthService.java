package com.techeerpicture.TecheerPicture.Instagram.service;

import com.techeerpicture.TecheerPicture.Instagram.dto.InstagramRequest;
import com.techeerpicture.TecheerPicture.Instagram.dto.InstagramTokenRequest;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class InstagramAuthService {

  private static final Logger logger = Logger.getLogger(InstagramAuthService.class.getName());
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${IG_APP_ID}")
  private String instagramAppId;

  @Value("${INSTAGRAM_APP_SECRET}")
  private String instagramAppSecret;

  @Value("${INSTAGRAM_REDIRECT_URI}")
  private String redirectUri;

  @Value("${INSTAGRAM_TOKEN_URL}")
  private String tokenUrl;

  /**
   * ✅ Instagram 로그인 URL 생성
   */
  public String getLoginUrl() {
    String cleanRedirectUri = cleanRedirectUri(redirectUri);

    return UriComponentsBuilder.fromHttpUrl("https://www.instagram.com/oauth/authorize")
        .queryParam("client_id", instagramAppId)
        .queryParam("redirect_uri", cleanRedirectUri)
        .queryParam("response_type", "code")
        .queryParam("scope", "instagram_business_basic,instagram_business_manage_messages,instagram_business_manage_comments,instagram_business_content_publish")
        .toUriString();
  }

  /**
   * ✅ Instagram OAuth Access Token 요청
   */
  public Map<String, Object> getAccessToken(String code) {
    logger.info("🔍 Instagram OAuth Access Token 요청 시작...");

    String cleanRedirectUri = cleanRedirectUri(redirectUri);

    // 📌 `application/x-www-form-urlencoded` 형식으로 요청
    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("client_id", instagramAppId);
    requestBody.add("client_secret", instagramAppSecret);
    requestBody.add("grant_type", "authorization_code");
    requestBody.add("redirect_uri", cleanRedirectUri);
    requestBody.add("code", code);

    // 📌 `Content-Type: application/x-www-form-urlencoded` 설정
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

    try {
      logger.info("📨 Instagram API 요청 URL: " + tokenUrl);
      logger.info("📨 요청 데이터: " + requestBody);

      ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);

      logger.info("✅ 응답 수신 성공! 응답 데이터: " + response.getBody());

      return parseResponse(response);
    } catch (HttpClientErrorException e) {
      logger.severe("❌ Instagram API 오류 발생! 응답 코드: " + e.getStatusCode());
      logger.severe("❌ 응답 본문: " + e.getResponseBodyAsString());

      throw new RuntimeException("Instagram API 오류 발생: " + e.getResponseBodyAsString());
    } catch (Exception e) {
      logger.severe("❌ 예기치 못한 오류 발생: " + e.getMessage());
      throw new RuntimeException("예기치 못한 오류 발생: " + e.getMessage());
    }
  }

  /**
   * ✅ JSON 응답을 Map으로 변환
   */
  private Map<String, Object> parseResponse(ResponseEntity<String> response) {
    JSONObject jsonResponse = new JSONObject(response.getBody());
    Map<String, Object> result = new HashMap<>();
    result.put("access_token", jsonResponse.optString("access_token", ""));
    result.put("user_id", jsonResponse.optString("user_id", ""));
    result.put("expires_in", jsonResponse.optInt("expires_in", 0));
    return result;
  }

  /**
   * ✅ `redirect_uri`에서 `#` 이후 부분 제거
   */
  private String cleanRedirectUri(String uri) {
    return uri.replaceAll("#.*$", "");
  }
}
