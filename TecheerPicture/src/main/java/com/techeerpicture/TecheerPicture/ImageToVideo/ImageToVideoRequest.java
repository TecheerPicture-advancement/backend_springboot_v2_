package com.techeerpicture.TecheerPicture.ImageToVideo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageToVideoRequest {

    @NotNull(message = "imageId는 필수 입력값입니다.")
    @Schema(description = "이미지 ID", example = "int")
    private Long imageId;

    @NotNull(message = "prompt는 필수 입력값입니다.")
    @Schema(description = "비디오 생성 프롬프트", example = "string")
    private String prompt;

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
