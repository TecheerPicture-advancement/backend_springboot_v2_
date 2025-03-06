package com.techeerpicture.TecheerPicture.RemoveBackground.controller;

import com.techeerpicture.TecheerPicture.RemoveBackground.dto.RemoveBackgroundRequest;
import com.techeerpicture.TecheerPicture.RemoveBackground.dto.RemoveBackgroundResponse;
import com.techeerpicture.TecheerPicture.RemoveBackground.service.RemoveBackgroundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/removebackgrounds")
@Tag(name = "Remove Background API", description = "이미지 배경 제거 API")
@RequiredArgsConstructor
public class RemoveBackgroundController {

  private final RemoveBackgroundService removeBackgroundService;

  @PostMapping
  @Operation(summary = "이미지 배경 제거", description = "이미지 ID를 받아 배경을 제거하고, 결과 이미지를 반환합니다.")
  public ResponseEntity<RemoveBackgroundResponse> removeBackground(@RequestBody RemoveBackgroundRequest request) {
    RemoveBackgroundResponse result = removeBackgroundService.removeBackground(request.getImageId());
    return ResponseEntity.ok(result);
  }
}
