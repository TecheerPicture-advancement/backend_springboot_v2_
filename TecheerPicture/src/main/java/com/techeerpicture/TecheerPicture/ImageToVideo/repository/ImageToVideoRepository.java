package com.techeerpicture.TecheerPicture.ImageToVideo.repository;

import com.techeerpicture.TecheerPicture.ImageToVideo.entity.ImageToVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageToVideoRepository extends JpaRepository<ImageToVideo, Long> {
}
