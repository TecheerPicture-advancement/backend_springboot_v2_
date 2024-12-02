package com.techeerpicture.TecheerPicture.Banner;

import com.techeerpicture.TecheerPicture.Banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BannerRepository
 *
 * 광고 배너 데이터를 관리하는 JPA 리포지토리 인터페이스입니다.
 * JpaRepository를 확장하여 기본적인 CRUD 기능을 제공하며,
 * 추가적인 쿼리 메서드를 정의하여 사용할 수 있습니다.
 *
 * @extends JpaRepository<Banner, Long>
 *   - Banner: 관리할 엔티티 클래스
 *   - Long: 엔티티의 기본 키 데이터 타입
 */
public interface BannerRepository extends JpaRepository<Banner, Long> {
}
