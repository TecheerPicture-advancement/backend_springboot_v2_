package com.techeerpicture.TecheerPicture.instagramtext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramTextRepository extends JpaRepository<InstagramText, Long> {
}
