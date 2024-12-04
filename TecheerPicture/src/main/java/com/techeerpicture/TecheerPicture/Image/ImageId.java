package com.techeerpicture.TecheerPicture.Image;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Data
@Embeddable
public class ImageId implements Serializable {
    private Long id;
    private Integer userId;

    // 반드시 기본 생성자와 equals, hashCode 메서드가 필요합니다.
    public ImageId() {}

    public ImageId(Long id, Integer userId) {
        this.id = id;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageId imageId = (ImageId) o;

        if (!id.equals(imageId.id)) return false;
        return userId.equals(imageId.userId);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }
}