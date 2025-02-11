package com.techeerpicture.TecheerPicture.Background;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import com.techeerpicture.TecheerPicture.Background.ImageTransform;

@Schema(description = "Pixelcut API 요청 데이터")
public class BackgroundRequest {
    private Long imageId;    // 이미지 ID
    private ImageTransform imageTransform;
    private String scene;
    private String prompt;

    public BackgroundRequest(Long imageId, ImageTransform imageTransform, String scene, String prompt) {
        this.imageId = imageId;
        this.imageTransform = imageTransform;
        this.scene = scene;
        this.prompt = prompt;
    }


    // Getters and Setters
    public ImageTransform getImageTransform() { return imageTransform; }
    public void setImageTransform(ImageTransform imageTransform) { this.imageTransform = imageTransform; }

    public String getScene() { return scene; }
    public void setScene(String scene) { this.scene = scene; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public Long getImageId() {
        return imageId;
    }
    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
