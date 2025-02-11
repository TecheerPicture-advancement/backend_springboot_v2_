package com.techeerpicture.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstagramRequest {

  @Schema(description = "외부에서 접근 가능한 이미지 URL", example = "https://example.com/200")
  private String image_url;

  @Schema(description = "Instagram 게시글 내용 (캡션)", example = "이것은 Instagram 캡션입니다!")
  private String PID_content;
}
