package com.techeerpicture.TecheerPicture.imageanalysis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageAnalysisRepository extends JpaRepository<ImageAnalysis, Long> {
}
