package com.techeerpicture.TecheerPicture.RecreatedBackground.entity;

import jakarta.persistence.*;

@Entity
public class RecreatedBackground {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long imageId;
    private double scale;
    private double xCenter;
    private double yCenter;
    private String imageUrl;
    private String scene;
    private String prompt;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted = false; // ✅ 삭제 여부 필드

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isRecreated = true; // ✅ 항상 재생성된 이미지로 설정

    public void setImageId(Long imageId) { this.imageId = imageId; }
    public void setScale(double scale) { this.scale = scale; }
    public void setXCenter(double xCenter) { this.xCenter = xCenter; }
    public void setYCenter(double yCenter) { this.yCenter = yCenter; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setScene(String scene) { this.scene = scene; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public void setIsDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }

    public Long getId() { return id; }
    public Long getImageId() { return imageId; }
    public double getScale() { return scale; }
    public double getXCenter() { return xCenter; }
    public double getYCenter() { return yCenter; }
    public String getImageUrl() { return imageUrl; }
    public String getScene() { return scene; }
    public String getPrompt() { return prompt; }
    public boolean getIsDeleted() { return isDeleted; }
    public boolean getIsRecreated() { return isRecreated; } // ✅ 항상 true
}
