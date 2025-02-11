package com.techeerpicture.TecheerPicture.Banner;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/banners")
@Tag(name = "Banner API", description = "광고 텍스트 생성 및 관리 API")
public class BannerController {

  @Autowired
  private BannerService bannerService;

  @Operation(summary = "광고 텍스트 생성", description = "입력된 정보를 바탕으로 광고 텍스트를 생성합니다.")
  @PostMapping
  public ResponseEntity<Map<String, Object>> createBanner(@RequestBody BannerRequest request) {
    try {
      Banner banner = bannerService.createBanner(request);

      // 응답 데이터 구성
      Map<String, Object> data = new LinkedHashMap<>();
      data.put("id", banner.getId());
      data.put("image_id", banner.getImage().getId()); // image_id 변경
      data.put("servetext", banner.getServText1());
      data.put("maintext", banner.getMainText1());
      data.put("servetext2", banner.getServText2());
      data.put("maintext2", banner.getMainText2());

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("code", 201);
      response.put("message", "배너 생성 성공");
      response.put("data", data);

      return ResponseEntity.status(201).body(response);
    } catch (Exception e) {
      // 오류 응답
      Map<String, Object> errorResponse = new LinkedHashMap<>();
      errorResponse.put("code", 500);
      errorResponse.put("message", "서버 내부 오류");
      errorResponse.put("error", e.getMessage());

      return ResponseEntity.status(500).body(errorResponse);
    }
  }

  @GetMapping("/{bannerId}")
  @Operation(summary = "광고 텍스트 조회", description = "배너 ID를 통해 광고 텍스트를 조회합니다.")
  public ResponseEntity<Map<String, Object>> getBanner(@PathVariable Long bannerId) {
    try {
      Banner banner = bannerService.getBannerById(bannerId);

      // 응답 데이터 구성
      Map<String, Object> data = new LinkedHashMap<>();
      data.put("id", banner.getId());
      data.put("image_id", banner.getImage().getId()); // image_id 변경
      data.put("servetext", banner.getServText1());
      data.put("maintext", banner.getMainText1());
      data.put("servetext2", banner.getServText2());
      data.put("maintext2", banner.getMainText2());

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("code", 200);
      response.put("message", "배너 조회 성공");
      response.put("data", data);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      Map<String, Object> errorResponse = new LinkedHashMap<>();
      errorResponse.put("code", 500);
      errorResponse.put("message", "배너 조회 실패");
      errorResponse.put("error", e.getMessage());

      return ResponseEntity.status(500).body(errorResponse);
    }
  }

  @PutMapping("/{bannerId}")
  @Operation(summary = "광고 텍스트 수정", description = "배너 ID를 통해 광고 텍스트를 수정합니다.")
  public ResponseEntity<Map<String, Object>> updateBanner(
      @PathVariable Long bannerId,
      @RequestBody BannerRequest request
  ) {
    try {
      Banner updatedBanner = bannerService.updateBanner(bannerId, request);

      // 응답 데이터 구성
      Map<String, Object> responseData = new LinkedHashMap<>();
      responseData.put("code", 200);
      responseData.put("message", "배너 수정 성공");
      responseData.put("data", Map.of(
          "id", updatedBanner.getId(),
          "image_id", updatedBanner.getImage().getId(), // image_id 변경
          "servetext", updatedBanner.getServText1(),
          "maintext", updatedBanner.getMainText1(),
          "servetext2", updatedBanner.getServText2(),
          "maintext2", updatedBanner.getMainText2()
      ));

      return ResponseEntity.ok(responseData);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(Map.of(
          "code", 500,
          "message", "배너 수정 실패",
          "error", e.getMessage()
      ));
    }
  }

  @DeleteMapping("/{bannerId}")
  @Operation(summary = "광고 텍스트 삭제", description = "배너 ID를 통해 광고 텍스트를 삭제합니다.")
  public ResponseEntity<Map<String, Object>> deleteBanner(@PathVariable Long bannerId) {
    try {
      bannerService.deleteBanner(bannerId);

      // 응답 데이터 구성
      Map<String, Object> responseData = new LinkedHashMap<>();
      responseData.put("code", 200);
      responseData.put("message", "배너 삭제 성공");

      return ResponseEntity.ok(responseData);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(Map.of(
          "code", 500,
          "message", "배너 삭제 실패",
          "error", e.getMessage()
      ));
    }
  }
}
