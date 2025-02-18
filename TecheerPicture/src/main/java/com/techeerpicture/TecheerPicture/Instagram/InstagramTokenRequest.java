package com.techeerpicture.TecheerPicture.Instagram;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstagramTokenRequest {

  @Schema(description = "Instagram Authorization Code", example = "AQDfzX_4_SpshVISYIBl...")
  private String code;
}
