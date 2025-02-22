package com.techeerpicture.TecheerPicture.imageanalysis;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageAnalysisUpdateRequest {

    @Schema(description = "수정할 이미지 분석 문장", example = "화이트 크림이 부드럽게 감싸진 3단 케이크. 따뜻한 조명이 비추는 우아한 공간 속에서 케이크가 화려하면서도 고급스러운 분위기를 자아낸다.")
    private String analysisText;
}
