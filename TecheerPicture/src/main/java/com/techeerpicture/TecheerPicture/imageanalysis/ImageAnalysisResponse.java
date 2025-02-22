package com.techeerpicture.TecheerPicture.imageanalysis;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ImageAnalysisResponse {

    @Schema(description = "이미지 분석 결과 설명", example = "contains a laptop on a desk with coffee")
    private String description;
}
