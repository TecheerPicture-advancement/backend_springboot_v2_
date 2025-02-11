package com.techeerpicture.TecheerPicture.Background;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techeerpicture.TecheerPicture.Image.Image;
import java.util.Optional;


@Repository
public interface BackgroundRepository extends JpaRepository<Background, Long> {
    // 특정 조건으로 이미지 조회
    @Query("SELECT i.imageUrl FROM Image i WHERE i.id = :id")
    String findImageUrlById(@Param("id") Long id);
    // id로 Background db 조회
    Optional<Background> findById(Long id);
}