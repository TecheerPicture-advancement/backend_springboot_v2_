package com.techeerpicture.TecheerPicture.Background;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PixelcutRequest {
    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("image_transform")
    private ImageTransform imageTransform;

    private String scene;
    private String prompt;

    @JsonProperty("negative_prompt")
    private String negativePrompt;

    public PixelcutRequest(String imageUrl, ImageTransform imageTransform, String scene, String prompt, String negativePrompt) {
        this.imageUrl = imageUrl;
        this.imageTransform = imageTransform;
        this.scene = scene;
        this.prompt = prompt;
        this.negativePrompt = negativePrompt;
    }

    // Getter 및 Setter
    public String getImageUrl() {
        return imageUrl;
    }

    public ImageTransform getImageTransform() {
        return imageTransform;
    }

    public String getScene() {
        return scene;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getNegativePrompt() { return negativePrompt; }
}
