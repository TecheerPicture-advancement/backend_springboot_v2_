package com.techeerpicture.TecheerPicture.Instagram.controller;

import com.techeerpicture.TecheerPicture.Instagram.dto.InstagramTokenRequest;
import com.techeerpicture.TecheerPicture.Instagram.service.InstagramAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/instagram")
@RequiredArgsConstructor
public class InstagramAuthController {

  private final InstagramAuthService instagramAuthService;

  @Operation(summary = "Instagram 로그인 URL 요청", description = "Instagram OAuth 로그인 URL을 반환합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "성공적으로 로그인 URL 반환"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @GetMapping("/login")
  public ResponseEntity<String> getLoginUrl() {
    return ResponseEntity.ok(instagramAuthService.getLoginUrl());
  }

  @Operation(summary = "Instagram OAuth Access Token 요청",
      description = "Authorization Code를 사용하여 Instagram Access Token을 요청합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "성공적으로 액세스 토큰 반환"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<Map<String, Object>> getAccessToken(@ModelAttribute InstagramTokenRequest request) {
    if (request.getCode() == null || request.getCode().isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("error", "Authorization code is required"));
    }
    return ResponseEntity.ok(instagramAuthService.getAccessToken(request.getCode()));
  }
}
