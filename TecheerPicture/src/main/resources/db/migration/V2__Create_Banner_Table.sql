CREATE TABLE banners (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         image_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         main_text_1 VARCHAR(255) NOT NULL,
                         serv_text_1 VARCHAR(255) NOT NULL,
                         main_text_2 VARCHAR(255) NOT NULL,
                         serv_text_2 VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
