package com.techeerpicture.TecheerPicture.InstagramText.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InstagramTextResponse {
    private Long id;
    private Long imageId;
    private String generatedText;
}
