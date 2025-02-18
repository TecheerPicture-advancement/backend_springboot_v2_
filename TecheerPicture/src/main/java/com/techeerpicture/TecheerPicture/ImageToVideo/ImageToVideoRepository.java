package com.techeerpicture.TecheerPicture.ImageToVideo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageToVideoRepository extends JpaRepository<ImageToVideo, Long> {
}
