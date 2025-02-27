package com.techeerpicture.TecheerPicture.RemoveBackground.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveBackgroundRequest {

  @Schema(description = "배경을 제거할 이미지의 ID", example = "1")
  private Long imageId;
}
