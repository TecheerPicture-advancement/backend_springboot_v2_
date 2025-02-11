CREATE TABLE background (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            image_id BIGINT NOT NULL,
                            scale DOUBLE NOT NULL,
                            x_center DOUBLE NOT NULL,
                            y_center DOUBLE NOT NULL,
                            image_url VARCHAR(255) NOT NULL,
                            scene VARCHAR(20),
                            prompt VARCHAR(500),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            is_deleted BOOLEAN DEFAULT FALSE,

                            CONSTRAINT fk_image FOREIGN KEY (image_id) REFERENCES image(id)
);