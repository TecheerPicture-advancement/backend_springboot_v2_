package com.techeerpicture.TecheerPicture.Backgrounds;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImageTransform {
    private double scale;
    private double xCenter;
    private double yCenter;

    // 기본 생성자
    public ImageTransform() {}

    // 파라미터를 받는 생성자
    public ImageTransform(double scale, double xCenter, double yCenter) {
        this.scale = scale;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
    }

    // Getter와 Setter
    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getXCenter() {
        return xCenter;
    }

    public void setXCenter(double xCenter) {
        this.xCenter = xCenter;
    }

    public double getYCenter() {
        return yCenter;
    }

    public void setYCenter(double yCenter) {
        this.yCenter = yCenter;
    }

    // toJson() 메서드 - 클래스 내부에서 정의해야 함
    public String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("Error converting ImageTransform to JSON", e);
        }
    }
}
