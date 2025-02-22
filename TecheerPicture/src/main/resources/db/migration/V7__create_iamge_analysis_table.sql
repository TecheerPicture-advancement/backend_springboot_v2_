CREATE TABLE image_analysis (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                image_url VARCHAR(500) NOT NULL,
                                analysis_text TEXT NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);
