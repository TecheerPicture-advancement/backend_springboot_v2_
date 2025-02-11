-- V1__init.sql
CREATE TABLE image (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       image_url VARCHAR(1000) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       is_deleted BOOLEAN DEFAULT FALSE
);
