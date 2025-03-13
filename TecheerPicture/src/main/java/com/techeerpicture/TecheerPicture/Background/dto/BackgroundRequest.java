package com.techeerpicture.TecheerPicture.Background.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import com.techeerpicture.TecheerPicture.Background.entity.ImageTransform;

public class BackgroundRequest {

    private Long imageId;
    private ImageTransform imageTransform;
    private String scene;
    private String prompt;
    private String negativePrompt;

    // 기본 생성자 추가
    public BackgroundRequest() {}

    // 기존의 BackgroundService 주입 방식 제거
    public BackgroundRequest(Long imageId, ImageTransform imageTransform, String scene, String prompt) {
        this.imageId = imageId;
        this.imageTransform = imageTransform;
        this.scene = scene;
        this.prompt = prompt;
    }

    // Getters and Setters
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public ImageTransform getImageTransform() {
        return imageTransform;
    }

    public void setImageTransform(ImageTransform imageTransform) {
        this.imageTransform = imageTransform;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getNegativePrompt() {
        return negativePrompt != null ? negativePrompt : "";
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }
}
