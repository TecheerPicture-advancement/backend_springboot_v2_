package com.techeerpicture.TecheerPicture.Banner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import com.techeerpicture.TecheerPicture.Banner.service.BannerService;
import com.techeerpicture.TecheerPicture.Banner.dto.BannerRequest;
import com.techeerpicture.TecheerPicture.Banner.entity.Banner;
import com.techeerpicture.TecheerPicture.Banner.dto.BannerBulkRequest;

@RestController
@RequestMapping("/api/v1/banners")
@Tag(name = "Banner API", description = "광고 텍스트 생성 및 관리 API")
public class BannerController {

  @Autowired
  private BannerService bannerService;

  @Operation(summary = "광고 텍스트 생성", description = "입력된 정보를 바탕으로 광고 텍스트를 생성합니다.")
  @PostMapping
  public ResponseEntity<Map<String, Object>> createBanner(@RequestBody BannerRequest request) {
    Banner banner = bannerService.createBanner(request);

    Map<String, Object> data = new LinkedHashMap<>();
    data.put("id", banner.getId());
    data.put("image_id", banner.getImage().getId());
    data.put("maintext", banner.getMainText1());
    data.put("servetext", banner.getServText1());
    data.put("maintext2", banner.getMainText2());
    data.put("servetext2", banner.getServText2());

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("code", 201);
    response.put("message", "배너 생성 성공");
    response.put("data", data);

    return ResponseEntity.status(201).body(response);
  }

  @PostMapping("/collection")
  public ResponseEntity<Map<String, Object>> createBannerCollection(@RequestBody BannerBulkRequest bulkRequest) {
    List<Banner> banners = bannerService.createBannersInParallel(bulkRequest.getRequests());

    List<Map<String, Object>> dataList = banners.stream().map(banner -> {
      Map<String, Object> data = new LinkedHashMap<>();
      data.put("id", banner.getId());
      data.put("image_id", banner.getImage().getId());
      data.put("maintext", banner.getMainText1());
      data.put("servetext", banner.getServText1());
      data.put("maintext2", banner.getMainText2());
      data.put("servetext2", banner.getServText2());
      return data;
    }).collect(Collectors.toList());

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("code", 201);
    response.put("message", "배너 다건 생성 성공");
    response.put("data", dataList);

    return ResponseEntity.status(201).body(response);
  }

  @GetMapping("/{bannerId}")
  @Operation(summary = "광고 텍스트 조회", description = "배너 ID를 통해 광고 텍스트를 조회합니다.")
  public ResponseEntity<Map<String, Object>> getBanner(@PathVariable Long bannerId) {
    try {
      Banner banner = bannerService.getBannerById(bannerId);

      Map<String, Object> data = new LinkedHashMap<>();
      data.put("id", banner.getId());
      data.put("image_id", banner.getImage().getId());
      data.put("maintext", banner.getMainText1());
      data.put("servetext", banner.getServText1());
      data.put("maintext2", banner.getMainText2());
      data.put("servetext2", banner.getServText2());

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

  @GetMapping("/image/{imageId}")
  @Operation(summary = "이미지 ID로 배너 조회", description = "이미지 ID로 연결된 배너 목록을 조회합니다. (N+1 발생 예시)")
  public ResponseEntity<Map<String, Object>> getBannersByImageId(@PathVariable Long imageId) {
    List<Banner> banners = bannerService.getBannersByImageId(imageId);

    List<Map<String, Object>> dataList = banners.stream().map(banner -> {
      Map<String, Object> data = new LinkedHashMap<>();
      data.put("id", banner.getId());
      data.put("image_id", banner.getImage().getId());
      data.put("maintext", banner.getMainText1());
      data.put("servetext", banner.getServText1());
      data.put("maintext2", banner.getMainText2());
      data.put("servetext2", banner.getServText2());
      return data;
    }).collect(Collectors.toList());

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("code", 200);
    response.put("message", "이미지 ID로 배너 조회 성공");
    response.put("data", dataList);

    return ResponseEntity.ok(response);
  }

  @PutMapping("/{bannerId}")
  @Operation(summary = "광고 텍스트 수정", description = "배너 ID를 통해 광고 텍스트를 수정합니다.")
  public ResponseEntity<Map<String, Object>> updateBanner(
      @PathVariable Long bannerId,
      @RequestBody BannerRequest request
  ) {
    try {
      Banner updatedBanner = bannerService.updateBanner(bannerId, request);

      Map<String, Object> data = new LinkedHashMap<>();
      data.put("id", updatedBanner.getId());
      data.put("image_id", updatedBanner.getImage().getId());
      data.put("maintext", updatedBanner.getMainText1());
      data.put("servetext", updatedBanner.getServText1());
      data.put("maintext2", updatedBanner.getMainText2());
      data.put("servetext2", updatedBanner.getServText2());

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("code", 200);
      response.put("message", "배너 수정 성공");
      response.put("data", data);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      Map<String, Object> error = new LinkedHashMap<>();
      error.put("code", 500);
      error.put("message", "배너 수정 실패");
      error.put("error", e.getMessage());

      return ResponseEntity.status(500).body(error);
    }
  }

  @DeleteMapping("/{bannerId}")
  @Operation(summary = "광고 텍스트 삭제", description = "배너 ID를 통해 광고 텍스트를 삭제합니다.")
  public ResponseEntity<Map<String, Object>> deleteBanner(@PathVariable Long bannerId) {
    try {
      bannerService.deleteBanner(bannerId);

      Map<String, Object> response = new LinkedHashMap<>();
      response.put("code", 200);
      response.put("message", "배너 삭제 성공");

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      Map<String, Object> error = new LinkedHashMap<>();
      error.put("code", 500);
      error.put("message", "배너 삭제 실패");
      error.put("error", e.getMessage());

      return ResponseEntity.status(500).body(error);
    }
  }
}
