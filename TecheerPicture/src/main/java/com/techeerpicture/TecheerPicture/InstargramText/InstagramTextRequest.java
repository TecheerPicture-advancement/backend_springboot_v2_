package com.techeerpicture.TecheerPicture.instagramtext;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstagramTextRequest {
    @Schema(description = "이미지 ID", example = "1")
    private Long imageId;

    @Schema(description = "사용자가 입력한 프롬프트", example = "단 3일간만 할인 행사 진행하는 맥북 프로")
    private String textPrompt;
}

