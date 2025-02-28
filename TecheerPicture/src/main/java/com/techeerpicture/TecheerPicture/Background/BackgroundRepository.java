package com.techeerpicture.TecheerPicture.Background;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techeerpicture.TecheerPicture.Image.Image;
import java.util.Optional;
import java.util.List;

@Repository
public interface BackgroundRepository extends JpaRepository<Background, Long> {
    // 특정 조건으로 이미지 조회
    @Query("SELECT b.imageUrl FROM Background b WHERE b.id = :id")
    Optional<String> findImageUrlById(@Param("id") Long id);
    List<Background> findByImageId(Long imageId);

}