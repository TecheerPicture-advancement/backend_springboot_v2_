package com.techeerpicture.TecheerPicture.Background;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
public class Background {

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
    private boolean isDeleted = false;

    @Column(nullable = true)
    private Long isRecreated;

    @Column(nullable = false)
    private String type = "generate"; // 기본값 설정

    // 기타 필드에 대한 Setter & Getter
    public void setImageId(Long imageId) { this.imageId = imageId; }
    public void setScale(double scale) { this.scale = scale; }
    public void setXCenter(double xCenter) { this.xCenter = xCenter; }
    public void setYCenter(double yCenter) { this.yCenter = yCenter; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setScene(String scene) { this.scene = scene; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public void setIsDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
    public void setIsRecreated(Long isRecreated) { this.isRecreated = isRecreated; }
    public void setType(String type) { this.type = type; } // 새로운 필드에 대한 Setter 추가

    public Long getId() { return id; }
    public Long getImageId() { return imageId; }
    public double getScale() { return scale; }
    public double getXCenter() { return xCenter; }
    public double getYCenter() { return yCenter; }
    public String getImageUrl() { return imageUrl; }
    public String getScene() { return scene; }
    public String getPrompt() { return prompt; }
    public boolean getIsDeleted() { return isDeleted; }
    public Long getIsRecreated() { return isRecreated; }
    public String getType() { return type; } // 새로운 필드에 대한 Getter 추가
}
