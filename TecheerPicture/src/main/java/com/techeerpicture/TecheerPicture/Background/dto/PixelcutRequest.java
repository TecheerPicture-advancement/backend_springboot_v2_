package com.techeerpicture.TecheerPicture.Background.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techeerpicture.TecheerPicture.Background.entity.ImageTransform;

public class PixelcutRequest {
    @JsonProperty("image_id") // JSON 키 이름을 API 요구사항에 맞게 설정
    private Long imageId;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("image_transform")
    private ImageTransform imageTransform;

    private String scene;

    private String prompt;

    @JsonProperty("negative_prompt")
    private String negativePrompt;

    public PixelcutRequest() {}

    public PixelcutRequest(Long imageId, String imageUrl, ImageTransform imageTransform, String scene, String prompt, String negativePrompt) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.imageTransform = imageTransform;
        this.scene = scene;
        this.prompt = prompt;
        this.negativePrompt = negativePrompt;
    }

    // Getters and Setters
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
        return negativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }
}
