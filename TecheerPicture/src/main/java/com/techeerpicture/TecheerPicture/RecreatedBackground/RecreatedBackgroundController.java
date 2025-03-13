package com.techeerpicture.TecheerPicture.RecreatedBackground;

import io.swagger.v3.oas.annotations.Operation;
import com.techeerpicture.TecheerPicture.Background.entity.Background;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recreated-backgrounds")
public class RecreatedBackgroundController {

  private final RecreatedBackgroundService recreatedBackgroundService;

  @Autowired
  public RecreatedBackgroundController(RecreatedBackgroundService recreatedBackgroundService) {
    this.recreatedBackgroundService = recreatedBackgroundService;
  }

  @Operation(summary = "배경 이미지 재생성", description = "기존 Background 데이터를 기반으로 새로운 배경을 재생성합니다.")
  @PostMapping("/{backgroundId}/recreate")
  public ResponseEntity<Background> recreateBackground(@PathVariable Long backgroundId) { // ✅ Background 반환
    Background recreatedBackground = recreatedBackgroundService.recreateBackground(backgroundId);
    return ResponseEntity.ok(recreatedBackground);
  }
}
