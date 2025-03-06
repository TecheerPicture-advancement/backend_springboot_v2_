-- V1__init.sql
CREATE TABLE image (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       image_url VARCHAR(1000) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       is_deleted BOOLEAN DEFAULT FALSE
);
INSERT INTO image (image_url, created_at, updated_at, is_deleted)
VALUES ('https://example.com/image1.jpg', NOW(), NOW(), 0);