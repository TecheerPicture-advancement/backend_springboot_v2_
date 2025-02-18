CREATE TABLE image_to_video (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                image_id BIGINT NOT NULL,
                                prompt VARCHAR(255) NOT NULL,
                                video_url VARCHAR(255) NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                is_deleted BOOLEAN DEFAULT FALSE,
                                CONSTRAINT fk_image_to_video FOREIGN KEY (image_id) REFERENCES image(id) ON DELETE CASCADE
);
