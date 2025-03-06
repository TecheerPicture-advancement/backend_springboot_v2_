package com.techeerpicture.TecheerPicture.RemoveBackground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RemoveBackgroundResponse {
  private int code;
  private String message;
  private Long backgroundId;
  private String resultUrl;
}
