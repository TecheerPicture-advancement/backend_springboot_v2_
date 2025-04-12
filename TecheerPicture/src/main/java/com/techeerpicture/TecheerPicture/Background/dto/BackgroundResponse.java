package com.techeerpicture.TecheerPicture.Background.dto;

public class BackgroundResponse {
    private Long id;
    private Long imageId;
    private String imageUrl;
    private String scene;
    private String prompt;

    // ✅ 5개 파라미터 받는 생성자
    public BackgroundResponse(Long id, Long imageId, String imageUrl, String scene, String prompt) {
        this.id = id;
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.scene = scene;
        this.prompt = prompt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
