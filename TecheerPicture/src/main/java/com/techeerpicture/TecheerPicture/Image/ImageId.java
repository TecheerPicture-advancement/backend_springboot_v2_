package com.techeerpicture.TecheerPicture.Image;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Data
@Embeddable
public class ImageId implements Serializable {
    private Long id;  // 이미지 ID만 사용

    // 반드시 기본 생성자와 equals, hashCode 메서드가 필요합니다.
    public ImageId() {}

    public ImageId(Long id) {
        this.id = id;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageId imageId = (ImageId) o;

        return id.equals(imageId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
