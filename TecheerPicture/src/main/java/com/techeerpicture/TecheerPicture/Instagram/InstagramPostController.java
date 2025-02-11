package com.techeerpicture.controller;

import com.techeerpicture.dto.InstagramRequest;
import com.techeerpicture.dto.PublishRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.util.Map;

@RestController
@RequestMapping("/api/instagram")
@Tag(name = "Instagram Post Controller", description = "Instagram 컨테이너 생성 및 게시")
public class InstagramPostController {

  @Value("${IG_ID}")
  private String igId;

  @Value("${ACCESS_TOKEN}")
  private String accessToken;

  @Operation(summary = "미디어 컨테이너 생성", description = "이미지 URL과 캡션을 사용하여 Instagram 미디어 컨테이너를 생성합니다.")
  @PostMapping("/create-container")
  public ResponseEntity<Map<String, Object>> createMediaContainer(@RequestBody InstagramRequest request) {
    String imageUrl = request.getImage_url();
    String caption = request.getPID_content();

    if (imageUrl == null || imageUrl.isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("error", "image_url 파라미터가 비어 있습니다."));
    }

    try {
      String url = "https://graph.instagram.com/v22.0/" + igId + "/media?access_token=" + accessToken;
      RestTemplate restTemplate = new RestTemplate();

      JSONObject requestBody = new JSONObject();
      requestBody.put("image_url", imageUrl);
      requestBody.put("caption", caption);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

      String response = restTemplate.postForObject(url, entity, String.class);
      JSONObject jsonResponse = new JSONObject(response);
      String containerId = jsonResponse.getString("id");

      return ResponseEntity.ok(Map.of("containerId", containerId));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(Map.of("error", "미디어 컨테이너 생성 중 오류 발생", "message", e.getMessage()));
    }
  }

  @Operation(summary = "미디어 컨테이너 게시", description = "생성된 미디어 컨테이너를 Instagram에 게시합니다.")
  @PostMapping("/publish-container")
  public ResponseEntity<Map<String, Object>> publishMediaContainer(@RequestBody PublishRequest request) {
    String containerId = request.getContainerId();

    if (containerId == null || containerId.isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("error", "containerId 파라미터가 비어 있습니다."));
    }

    try {
      String url = "https://graph.instagram.com/v22.0/" + igId + "/media_publish?access_token=" + accessToken;
      RestTemplate restTemplate = new RestTemplate();

      JSONObject requestBody = new JSONObject();
      requestBody.put("creation_id", containerId);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

      String response = restTemplate.postForObject(url, entity, String.class);
      JSONObject jsonResponse = new JSONObject(response);
      String mediaId = jsonResponse.getString("id");

      return ResponseEntity.ok(Map.of("mediaId", mediaId));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(Map.of("error", "미디어 게시 중 오류 발생", "message", e.getMessage()));
    }
  }
}
