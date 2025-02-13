-- V4__update_banner_table.sql

-- `banners` 테이블이 존재하면 수정
ALTER TABLE banners
    ADD COLUMN item_name VARCHAR(255) NOT NULL,
    ADD COLUMN item_concept VARCHAR(255) NOT NULL,
    ADD COLUMN item_category VARCHAR(255) NOT NULL,
    ADD COLUMN prompt VARCHAR(500) NOT NULL;
