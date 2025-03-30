package com.techeerpicture.TecheerPicture.Image.repository;

import com.techeerpicture.TecheerPicture.Image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

  @Query("SELECT i FROM Image i WHERE i.id = :id AND i.isDeleted = false")
  Optional<Image> findActiveImageById(@Param("id") Long id);
}
