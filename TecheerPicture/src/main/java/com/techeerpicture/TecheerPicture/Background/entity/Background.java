package com.techeerpicture.TecheerPicture.Background.entity;

import com.techeerpicture.TecheerPicture.Background.entity.Background;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonProperty("isDeleted")
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted = false;

    private Long isRecreated;

    @Column(nullable = false)
    private String type = "generate";

    // ✅ 필요한 생성자 추가
    public Background(Long imageId, String imageUrl) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
    }

    public void setTypeToRemove() {
        this.type = "remove";
    }
}
