package com.techeerpicture.TecheerPicture.Background;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Objects;

@Entity
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

    public void setImageId(Long imageId) {this.imageId = imageId;}

    public void setScale(double scale) {this.scale = scale;}

    public void setXCenter(double xCenter) {this.xCenter = xCenter;}

    public void setYCenter(double yCenter) {this.yCenter = yCenter;}

    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public void setScene(String scene) {this.scene = scene;}

    public void setPrompt(String prompt) {this.prompt = prompt;}

    public Long getImageId() {return imageId;}

    public double getScale() {return scale;}

    public double getXCenter() {return xCenter;}

    public double getYCenter() {return yCenter;}

    public String getImageUrl() {return imageUrl;}

    public String getScene() {return scene;}

    public String getPrompt() {return prompt;}

    public boolean getIsDeleted() { return isDeleted; }

    public void setIsDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }

}
