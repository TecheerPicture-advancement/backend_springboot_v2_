package com.techeerpicture.TecheerPicture.Banner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.techeerpicture.TecheerPicture.Image.Image;
import com.techeerpicture.TecheerPicture.Image.ImageRepository;

import com.techeerpicture.TecheerPicture.Banner.repository.BannerRepository;
import com.techeerpicture.TecheerPicture.Banner.dto.BannerRequest;
import com.techeerpicture.TecheerPicture.Banner.entity.Banner;
import com.techeerpicture.TecheerPicture.Banner.external.GPTService;
import com.techeerpicture.TecheerPicture.Banner.util.GeneratedTexts;


@Service
public class BannerService {

  @Autowired
  private BannerRepository bannerRepository;

  @Autowired
  private GPTService gptService; // GPTService 주입

  @Autowired
  private ImageRepository imageRepository; // ImageRepository 주입

  public Banner createBanner(BannerRequest request) {
    // Image 엔티티 조회
    Image image = imageRepository.findById(request.getImageId())
        .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));


    String imageUrl = image.getImageUrl();
    // GPT를 이용해 광고 문구 생성
    GeneratedTexts texts = gptService.generateAdTexts(
        request.getItemName(),
        request.getItemConcept(),
        request.getItemCategory(),
        request.getAddInformation(),
        imageUrl
    );

    // Banner 객체 생성 및 값 설정
    Banner banner = new Banner();
    banner.setMainText1(texts.getMainText1());
    banner.setServText1(texts.getServText1());
    banner.setMainText2(texts.getMainText2());
    banner.setServText2(texts.getServText2());
    banner.setItemName(request.getItemName());
    banner.setItemConcept(request.getItemConcept());
    banner.setItemCategory(request.getItemCategory());
    banner.setPrompt(request.getAddInformation()); //add_information을 prompt로 저장
    banner.setImage(image); // Image 설정

    return bannerRepository.save(banner);
  }

  public Banner getBannerById(Long bannerId) {
    return bannerRepository.findById(bannerId)
        .orElseThrow(() -> new RuntimeException("배너를 찾을 수 없습니다."));
  }

  public Banner updateBanner(Long bannerId, BannerRequest request) {
    return bannerRepository.findById(bannerId).map(banner -> {
      // Image 엔티티 조회
      Image image = imageRepository.findById(request.getImageId())
          .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));

      String imageUrl = image.getImageUrl();
      // GPT를 이용해 광고 문구 재생성
      GeneratedTexts texts = gptService.generateAdTexts(
          request.getItemName(),
          request.getItemConcept(),
          request.getItemCategory(),
          request.getAddInformation(),
          imageUrl
      );

      // Banner 객체 업데이트
      banner.setMainText1(texts.getMainText1());
      banner.setServText1(texts.getServText1());
      banner.setMainText2(texts.getMainText2());
      banner.setServText2(texts.getServText2());
      banner.setItemName(request.getItemName());
      banner.setItemConcept(request.getItemConcept());
      banner.setItemCategory(request.getItemCategory());
      banner.setPrompt(request.getAddInformation()); //add_information을 prompt로 저장
      banner.setImage(image); // Image 업데이트

      return bannerRepository.save(banner);
    }).orElseThrow(() -> new RuntimeException("배너를 찾을 수 없습니다."));
  }

  public void deleteBanner(Long bannerId) {
    Banner banner = bannerRepository.findById(bannerId)
        .orElseThrow(() -> new RuntimeException("배너를 찾을 수 없습니다."));
    bannerRepository.delete(banner);
  }
}
