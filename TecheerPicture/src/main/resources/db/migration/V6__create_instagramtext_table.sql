CREATE TABLE instagramtext (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               image_id BIGINT NOT NULL,
                               generated_text VARCHAR(1000) NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               is_deleted BOOLEAN DEFAULT FALSE NOT NULL,

    -- `image_id` 외래 키 설정
                               CONSTRAINT fk_instagramtext_image FOREIGN KEY (image_id) REFERENCES image(id) ON DELETE CASCADE
);
