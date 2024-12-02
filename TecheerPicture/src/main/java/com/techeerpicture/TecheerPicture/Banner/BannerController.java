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
@Tag(name = "Banner API", description = "광고 텍스트 생성 및 관리 API") // Swagger API 문서 태그 설정
public class BannerController {

  @Autowired
  private BannerService bannerService; // BannerService 주입

  /**
   * 광고 텍스트 생성
   * 입력된 요청 데이터를 기반으로 광고 텍스트를 생성하고 생성된 배너 정보를 반환합니다.
   *
   * @param request BannerRequest 객체 (생성에 필요한 데이터 포함)
   * @return 생성된 배너 데이터와 HTTP 상태 코드
   */
  @Operation(summary = "광고 텍스트 생성", description = "입력된 정보를 바탕으로 광고 텍스트를 생성합니다.")
  @PostMapping
  public ResponseEntity<Map<String, Object>> createBanner(@RequestBody BannerRequest request) {
    try {
      Banner banner = bannerService.createBanner(request);

      // 응답 데이터 구성
      Map<String, Object> data = new LinkedHashMap<>();
      data.put("id", banner.getId());
      data.put("user_id", banner.getUserId());
      data.put("image_id", banner.getImageId());
      data.put("servetext", banner.getServText1());
      data.put("maintext", banner.getMainText1());
      data.put("servetext2", banner.getServText2());
      data.put("maintext2", banner.getMainText2());

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("code", 201); // HTTP 201 Created
      response.put("message", "배너 생성 성공");
      response.put("data", data);

      return ResponseEntity.status(201).body(response); // HTTP 201 응답
    } catch (Exception e) {
      // 오류 발생 시 응답 처리
      Map<String, Object> errorResponse = new LinkedHashMap<>();
      errorResponse.put("code", 500); // HTTP 500 Internal Server Error
      errorResponse.put("message", "서버 내부 오류");
      errorResponse.put("error", e.getMessage());

      return ResponseEntity.status(500).body(errorResponse); // HTTP 500 응답
    }
  }

  /**
   * 광고 텍스트 조회
   * 주어진 배너 ID를 기반으로 해당 광고 텍스트를 조회합니다.
   *
   * @param bannerId 조회할 배너의 ID
   * @return 조회된 배너 데이터와 HTTP 상태 코드
   */
  @GetMapping("/{bannerId}")
  @Operation(summary = "광고 텍스트 조회", description = "배너 ID를 통해 광고 텍스트를 조회합니다.")
  public ResponseEntity<Map<String, Object>> getBanner(@PathVariable Long bannerId) {
    try {
      Banner banner = bannerService.getBannerById(bannerId);

      // 응답 데이터 구성
      Map<String, Object> data = new LinkedHashMap<>();
      data.put("id", banner.getId());
      data.put("user_id", banner.getUserId());
      data.put("image_id", banner.getImageId());
      data.put("servetext", banner.getServText1());
      data.put("maintext", banner.getMainText1());
      data.put("servetext2", banner.getServText2());
      data.put("maintext2", banner.getMainText2());

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("code", 200); // HTTP 200 OK
      response.put("message", "배너 조회 성공");
      response.put("data", data);

      return ResponseEntity.ok(response); // HTTP 200 응답
    } catch (Exception e) {
      // 오류 발생 시 응답 처리
      Map<String, Object> errorResponse = new LinkedHashMap<>();
      errorResponse.put("code", 500); // HTTP 500 Internal Server Error
      errorResponse.put("message", "배너 조회 실패");
      errorResponse.put("error", e.getMessage());

      return ResponseEntity.status(500).body(errorResponse); // HTTP 500 응답
    }
  }

  /**
   * 광고 텍스트 수정
   * 주어진 배너 ID와 요청 데이터를 기반으로 광고 텍스트를 수정합니다.
   *
   * @param bannerId 수정할 배너의 ID
   * @param request  수정할 데이터 요청 객체
   * @return 수정된 배너 데이터와 HTTP 상태 코드
   */
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
      responseData.put("code", 200); // HTTP 200 OK
      responseData.put("message", "배너 수정 성공");
      responseData.put("data", Map.of(
          "id", updatedBanner.getId(),
          "user_id", updatedBanner.getUserId(),
          "image_id", updatedBanner.getImageId(),
          "servetext", updatedBanner.getServText1(),
          "maintext", updatedBanner.getMainText1(),
          "servetext2", updatedBanner.getServText2(),
          "maintext2", updatedBanner.getMainText2()
      ));

      return ResponseEntity.ok(responseData); // HTTP 200 응답
    } catch (Exception e) {
      // 오류 발생 시 응답 처리
      return ResponseEntity.status(500).body(Map.of(
          "code", 500, // HTTP 500 Internal Server Error
          "message", "배너 수정 실패",
          "error", e.getMessage()
      ));
    }
  }

  /**
   * 광고 텍스트 삭제
   * 주어진 배너 ID를 기반으로 광고 텍스트를 삭제합니다.
   *
   * @param bannerId 삭제할 배너의 ID
   * @return 삭제 성공 메시지와 HTTP 상태 코드
   */
  @DeleteMapping("/{bannerId}")
  @Operation(summary = "광고 텍스트 삭제", description = "배너 ID를 통해 광고 텍스트를 삭제합니다.")
  public ResponseEntity<Map<String, Object>> deleteBanner(@PathVariable Long bannerId) {
    try {
      bannerService.deleteBanner(bannerId);

      // 응답 데이터 구성
      Map<String, Object> responseData = new LinkedHashMap<>();
      responseData.put("code", 200); // HTTP 200 OK
      responseData.put("message", "배너 삭제 성공");

      return ResponseEntity.ok(responseData); // HTTP 200 응답
    } catch (Exception e) {
      // 오류 발생 시 응답 처리
      return ResponseEntity.status(500).body(Map.of(
          "code", 500, // HTTP 500 Internal Server Error
          "message", "배너 삭제 실패",
          "error", e.getMessage()
      ));
    }
  }
}
