package com.techeerpicture.TecheerPicture.Instagram.controller;

import com.techeerpicture.TecheerPicture.Instagram.dto.InstagramRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/instagram")
@Tag(name = "Instagram Post Controller", description = "Instagram 컨테이너 생성 및 게시")
public class InstagramPostController {

  private final RestTemplate restTemplate = new RestTemplate();

  @Operation(summary = "Instagram 게시 (단일 및 캐러셀 지원)", description = "이미지 리스트를 받아 컨테이너 생성 후 자동 게시합니다.")
  @PostMapping("/pid")
  public ResponseEntity<Map<String, Object>> createAndPublishMedia(
      @RequestParam String access_token,
      @RequestParam String user_id,
      @RequestBody InstagramRequest request) {

    List<String> imageUrls = request.getImageUrls();
    String caption = request.getPidContent();

    if (imageUrls == null || imageUrls.isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("error", "imageUrls 리스트가 비어 있습니다."));
    }

    // ✅ 단일 이미지 게시
    if (imageUrls.size() == 1) {
      String imageUrl = imageUrls.get(0);
      try {
        String containerId = createMediaContainer(imageUrl, caption, false, user_id, access_token);
        if (containerId == null) {
          return ResponseEntity.status(500).body(Map.of("error", "미디어 컨테이너 생성 실패"));
        }

        String mediaId = publishMediaContainer(containerId, user_id, access_token);
        String permalink = getMediaPermalink(mediaId, access_token);

        return ResponseEntity.ok(Map.of(
            "success", "단일 이미지 게시 완료",
            "mediaId", mediaId,
            "permalink", permalink
        ));
      } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(Map.of(
            "error", "단일 이미지 게시 중 오류 발생",
            "message", e.getMessage()
        ));
      }
    }

    // ✅ 여러 이미지(캐러셀) 처리
    List<String> containerIds = new ArrayList<>();
    for (String imageUrl : imageUrls) {
      try {
        String mediaContainerId = createMediaContainer(imageUrl, null, true, user_id, access_token);
        if (mediaContainerId != null) {
          containerIds.add(mediaContainerId);
        }
      } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(Map.of("error", "캐러셀 미디어 컨테이너 생성 중 오류 발생", "message", e.getMessage()));
      }
    }

    if (containerIds.isEmpty()) {
      return ResponseEntity.status(500).body(Map.of("error", "모든 미디어 컨테이너 생성이 실패했습니다."));
    }

    // ✅ 캐러셀 컨테이너 생성
    String carouselContainerId;
    try {
      carouselContainerId = createCarouselContainer(containerIds, caption, user_id, access_token);
      if (carouselContainerId == null) {
        return ResponseEntity.status(500).body(Map.of("error", "캐러셀 컨테이너 생성 실패"));
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(Map.of("error", "캐러셀 컨테이너 생성 중 오류 발생", "message", e.getMessage()));
    }

    // ✅ 캐러셀 게시
    try {
      String mediaId = publishMediaContainer(carouselContainerId, user_id, access_token);
      String permalink = getMediaPermalink(mediaId, access_token);

      return ResponseEntity.ok(Map.of(
          "success", "캐러셀 게시 완료",
          "mediaId", mediaId,
          "permalink", permalink
      ));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(Map.of("error", "캐러셀 게시 중 오류 발생", "message", e.getMessage()));
    }
  }

  // 🔧 미디어 컨테이너 생성
  private String createMediaContainer(String imageUrl, String caption, boolean isCarouselItem, String userId, String accessToken) {
    String url = "https://graph.instagram.com/v22.0/" + userId + "/media?access_token=" + accessToken;

    JSONObject requestBody = new JSONObject();
    requestBody.put("image_url", imageUrl);
    if (isCarouselItem) requestBody.put("is_carousel_item", true);
    if (caption != null) requestBody.put("caption", caption);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

    String response = restTemplate.postForObject(url, entity, String.class);
    JSONObject jsonResponse = new JSONObject(response);
    return jsonResponse.optString("id", null);
  }

  // 🔧 캐러셀 컨테이너 생성
  private String createCarouselContainer(List<String> containerIds, String caption, String userId, String accessToken) {
    String url = "https://graph.instagram.com/v22.0/" + userId + "/media?access_token=" + accessToken;

    JSONObject requestBody = new JSONObject();
    requestBody.put("media_type", "CAROUSEL");
    requestBody.put("caption", caption);
    requestBody.put("children", String.join(",", containerIds));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

    String response = restTemplate.postForObject(url, entity, String.class);
    JSONObject jsonResponse = new JSONObject(response);
    return jsonResponse.optString("id", null);
  }

  // 🔧 미디어 게시
  private String publishMediaContainer(String containerId, String userId, String accessToken) {
    String url = "https://graph.instagram.com/v22.0/" + userId + "/media_publish?access_token=" + accessToken;

    JSONObject requestBody = new JSONObject();
    requestBody.put("creation_id", containerId);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

    String response = restTemplate.postForObject(url, entity, String.class);
    JSONObject jsonResponse = new JSONObject(response);
    return jsonResponse.optString("id", null);
  }

  // 🆕 게시된 mediaId로 permalink 가져오기
  private String getMediaPermalink(String mediaId, String accessToken) {
    String url = "https://graph.instagram.com/" + mediaId + "?fields=permalink&access_token=" + accessToken;
    String response = restTemplate.getForObject(url, String.class);
    JSONObject jsonResponse = new JSONObject(response);
    return jsonResponse.optString("permalink", null);
  }
}
