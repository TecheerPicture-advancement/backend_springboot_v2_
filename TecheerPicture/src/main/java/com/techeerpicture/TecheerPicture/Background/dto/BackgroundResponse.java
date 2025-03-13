package com.techeerpicture.TecheerPicture.Background.dto;

public class BackgroundResponse {
    private Long id;
    private String imageUrl;
    private String scene;
    private String prompt;

    // 매개변수를 받는 생성자 추가
    public BackgroundResponse(Long id, String imageUrl, String scene, String prompt) {
        this.id = id;
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
