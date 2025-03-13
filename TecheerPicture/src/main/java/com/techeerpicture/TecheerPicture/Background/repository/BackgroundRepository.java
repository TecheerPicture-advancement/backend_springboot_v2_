package com.techeerpicture.TecheerPicture.Background.repository;

import com.techeerpicture.TecheerPicture.Background.entity.Background; //추가한 내용
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techeerpicture.TecheerPicture.Image.entity.Image;
import java.util.Optional;
import java.util.List;

@Repository
public interface BackgroundRepository extends JpaRepository<Background, Long> {
    // ✅ Optional<Background>으로 수정하여 orElseGet 사용 가능
    Optional<Background> findByImageId(Long imageId);
}