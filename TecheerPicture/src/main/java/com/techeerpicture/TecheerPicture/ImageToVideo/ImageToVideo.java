package com.techeerpicture.TecheerPicture.ImageToVideo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import com.techeerpicture.TecheerPicture.Image.Image;


@Entity
@Table(name = "image_to_video")
@Getter
@Setter
@NoArgsConstructor
public class ImageToVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Column(nullable = false, length = 50)
    private String prompt;

    @Column(name = "video_url", length = 255, nullable = false)
    private String videoUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
