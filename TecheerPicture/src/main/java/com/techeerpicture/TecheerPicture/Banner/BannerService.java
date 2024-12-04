package com.techeerpicture.TecheerPicture.Banner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerService {

  @Autowired
  private BannerRepository bannerRepository;

  @Autowired
  private GPTService gptService; // GPTService 주입

  public Banner createBanner(BannerRequest request) {
    // GPT를 이용해 광고 문구 생성
    GeneratedTexts texts = gptService.generateAdTexts(
        request.getItemName(),
        request.getItemConcept(),
        request.getItemCategory(),
        request.getAddInformation()
    );

    // Banner 객체 생성 및 값 설정
    Banner banner = new Banner();
    banner.setMainText1(texts.getMainText1());
    banner.setServText1(texts.getServText1());
    banner.setMainText2(texts.getMainText2());
    banner.setServText2(texts.getServText2());
    banner.setImageId(request.getImageId());
    banner.setUserId(request.getUserId());

    return bannerRepository.save(banner);
  }

  public Banner getBannerById(Long bannerId) {
    return bannerRepository.findById(bannerId)
        .orElseThrow(() -> new RuntimeException("배너를 찾을 수 없습니다."));
  }

  public Banner updateBanner(Long bannerId, BannerRequest request) {
    return bannerRepository.findById(bannerId).map(banner -> {
      // GPT를 이용해 광고 문구 재생성
      GeneratedTexts texts = gptService.generateAdTexts(
          request.getItemName(),
          request.getItemConcept(),
          request.getItemCategory(),
          request.getAddInformation()
      );

      // Banner 객체 업데이트
      banner.setMainText1(texts.getMainText1());
      banner.setServText1(texts.getServText1());
      banner.setMainText2(texts.getMainText2());
      banner.setServText2(texts.getServText2());
      banner.setImageId(request.getImageId());
      banner.setUserId(request.getUserId());

      return bannerRepository.save(banner);
    }).orElseThrow(() -> new RuntimeException("배너를 찾을 수 없습니다."));
  }

  public void deleteBanner(Long bannerId) {
    Banner banner = bannerRepository.findById(bannerId)
        .orElseThrow(() -> new RuntimeException("배너를 찾을 수 없습니다."));
    bannerRepository.delete(banner);
  }
}