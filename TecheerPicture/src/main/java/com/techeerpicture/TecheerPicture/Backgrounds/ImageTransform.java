package com.techeerpicture.TecheerPicture.Background;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이미지 변환 정보")
public class ImageTransform {
    private double scale;
    private double x_center;
    private double y_center;

    // 기본 생성자
    public ImageTransform() {}

    // 매개변수를 받는 생성자
    public ImageTransform(double scale, double x_center, double y_center) {
        this.scale = scale;
        this.x_center = x_center;
        this.y_center = y_center;
    }

    // Getter와 Setter
    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getX_center() {
        return x_center;
    }

    public void setX_center(double x_center) {
        this.x_center = x_center;
    }

    public double getY_center() {
        return y_center;
    }

    public void setY_center(double y_center) {
        this.y_center = y_center;
    }
}