package com.techeerpicture.TecheerPicture.Instagram.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InstagramRequest {

  @Schema(description = "외부에서 접근 가능한 이미지 URL 목록 (단일 또는 여러 개 가능)", example = "[\"https://example.com/200\", \"https://example.com/201\"]")
  private List<String> imageUrls;

  @Schema(description = "Instagram 게시글 내용 (캡션)", example = "이것은 Instagram 캡션입니다!")
  private String PID_content;
}
