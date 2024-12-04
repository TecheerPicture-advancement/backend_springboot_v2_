CREATE TABLE image (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       user_id BIGINT NOT NULL,
                       image_url VARCHAR(1000) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       is_deleted BOOLEAN DEFAULT FALSE,
                       FOREIGN KEY (user_id) REFERENCES users(id)
                           ON DELETE CASCADE
                           ON UPDATE CASCADE
);
