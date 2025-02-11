package com.techeerpicture.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishRequest {

  @Schema(description = "생성된 미디어 컨테이너의 ID", example = "int")
  private String containerId;
}
