package com.techeerpicture.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONArray;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/instagram")
@Tag(name = "Instagram API", description = "Instagram 미디어 데이터 제공 API")
public class InstagramController {

  @Operation(summary = "Instagram 미디어 가져오기", description = "Instagram 계정에서 미디어 데이터를 가져옵니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "미디어 데이터를 성공적으로 반환"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (필수 파라미터 누락)"),
      @ApiResponse(responseCode = "500", description = "서버 오류 발생")
  })
  @GetMapping("/media")
  public ResponseEntity<List<Map<String, Object>>> getInstagramMedia(
      @RequestParam String access_token,
      @RequestParam String user_id) {

    String mediaUrl = "https://graph.instagram.com/v22.0/" + user_id + "/media?access_token=" + access_token;

    // API 요청
    RestTemplate restTemplate = new RestTemplate();
    String response = restTemplate.getForObject(mediaUrl, String.class);

    // JSON 응답 파싱
    JSONObject jsonResponse = new JSONObject(response);
    JSONArray data = jsonResponse.getJSONArray("data");

    List<Map<String, Object>> mediaList = new ArrayList<>();

    // 각 미디어 객체 순회
    for (int i = 0; i < data.length(); i++) {
      JSONObject mediaObject = data.getJSONObject(i);
      String mediaId = mediaObject.getString("id");

      // 각 미디어 객체의 세부 정보를 가져오기 위한 추가 요청
      String mediaDetailsUrl = "https://graph.instagram.com/v22.0/" + mediaId +
          "?fields=id,media_type,media_url,thumbnail_url,caption,permalink&access_token=" + access_token;

      String mediaDetailsResponse = restTemplate.getForObject(mediaDetailsUrl, String.class);
      JSONObject mediaDetails = new JSONObject(mediaDetailsResponse);

      // 리스트에 추가
      mediaList.add(Map.of(
          "id", mediaDetails.getString("id"),
          "mediaType", mediaDetails.getString("media_type"),
          "mediaUrl", mediaDetails.optString("media_url", ""),
          "thumbnailUrl", mediaDetails.optString("thumbnail_url", ""),
          "caption", mediaDetails.optString("caption", "No Caption"),
          "permalink", mediaDetails.getString("permalink")
      ));
    }

    return ResponseEntity.ok(mediaList);
  }
}
