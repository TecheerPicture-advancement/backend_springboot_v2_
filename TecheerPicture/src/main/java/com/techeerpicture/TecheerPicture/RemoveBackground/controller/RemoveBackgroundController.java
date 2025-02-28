package com.techeerpicture.TecheerPicture.RemoveBackground.controller;

import com.techeerpicture.TecheerPicture.RemoveBackground.dto.RemoveBackgroundRequest;
import com.techeerpicture.TecheerPicture.RemoveBackground.service.RemoveBackgroundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/removebackgrounds")
@Tag(name = "Remove Background API", description = "이미지 배경 제거 API")
@RequiredArgsConstructor
public class RemoveBackgroundController {

  private final RemoveBackgroundService removeBackgroundService;

  @PostMapping
  @Operation(summary = "이미지 배경 제거", description = "이미지 ID를 받아 배경을 제거하고, 결과 이미지를 반환합니다.")
  public ResponseEntity<Map<String, Object>> removeBackground(@RequestBody RemoveBackgroundRequest request) {
    try {
      String resultUrl = removeBackgroundService.removeBackground(request.getImageId());

      Map<String, Object> responseData = new LinkedHashMap<>();
      responseData.put("code", 200);
      responseData.put("message", "배경 제거 성공");
      responseData.put("result_url", resultUrl);

      return ResponseEntity.ok(responseData);
    } catch (Exception e) {
      Map<String, Object> errorResponse = new LinkedHashMap<>();
      errorResponse.put("code", 500);
      errorResponse.put("message", "배경 제거 실패");
      errorResponse.put("error", e.getMessage());

      return ResponseEntity.status(500).body(errorResponse);
    }
  }
}
