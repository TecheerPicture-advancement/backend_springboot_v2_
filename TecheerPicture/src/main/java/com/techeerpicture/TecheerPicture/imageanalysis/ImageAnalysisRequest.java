package com.techeerpicture.TecheerPicture.imageanalysis;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageAnalysisRequest {

    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
    private String imageUrl;
}
