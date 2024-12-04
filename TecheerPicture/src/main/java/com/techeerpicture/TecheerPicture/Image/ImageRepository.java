package com.techeerpicture.TecheerPicture.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// FileEntity를 엔티티로 사용하는 리포지토리
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}