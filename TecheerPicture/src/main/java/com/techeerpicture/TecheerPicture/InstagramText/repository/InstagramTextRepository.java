package com.techeerpicture.TecheerPicture.InstagramText.repository;

import com.techeerpicture.TecheerPicture.InstagramText.entity.InstagramText;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramTextRepository extends JpaRepository<InstagramText, Long> {
}
