package com.techeerpicture.TecheerPicture.Background;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // JSON에서 인식되지 않는 필드를 무시
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

    @JsonProperty("isDeleted") // JSON 필드 "isDeleted"와 매핑
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted = false;

    private Long isRecreated;

    @Column(nullable = false)
    private String type = "generate"; // 기본값 설정

    public void setTypeToRemove() {
        this.type = "remove";
    }
}
