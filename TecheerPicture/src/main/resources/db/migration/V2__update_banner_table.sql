-- V2__update_banner_table.sql

-- `banners` 테이블 삭제 및 재생성
DROP TABLE IF EXISTS banners;

CREATE TABLE banners (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         image_id BIGINT NOT NULL,
                         main_text_1 VARCHAR(255) NOT NULL,
                         serv_text_1 VARCHAR(255) NOT NULL,
                         main_text_2 VARCHAR(255) NOT NULL,
                         serv_text_2 VARCHAR(255) NOT NULL,
                         is_deleted BOOLEAN NOT NULL DEFAULT FALSE,  -- 삭제 여부 플래그 필드 추가
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 외래 키 설정 (이미지 테이블과 연관된 경우)
ALTER TABLE banners
    ADD CONSTRAINT fk_image_id FOREIGN KEY (image_id) REFERENCES image(id) ON DELETE CASCADE;
