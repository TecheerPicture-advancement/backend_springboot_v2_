package com.techeerpicture.TecheerPicture.Banner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.techeerpicture.TecheerPicture.Image.entity.Image;
import com.techeerpicture.TecheerPicture.Image.repository.ImageRepository;

import com.techeerpicture.TecheerPicture.Banner.repository.BannerRepository;
import com.techeerpicture.TecheerPicture.Banner.dto.BannerRequest;
import com.techeerpicture.TecheerPicture.Banner.entity.Banner;
import com.techeerpicture.TecheerPicture.Banner.external.GPTService;
import com.techeerpicture.TecheerPicture.Banner.util.GeneratedTexts;

import java.util.concurrent.CompletableFuture;

@Service
public class BannerService {

  @Autowired
  private BannerRepository bannerRepository;

  @Autowired
  private GPTService gptService; // GPTService 주입

  @Autowired
  private ImageRepository imageRepository; // ImageRepository 주입

  public Banner createBanner(BannerRequest request) {
    Image image = imageRepository.findById(request.getImageId())
        .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));
    String imageUrl = image.getImageUrl();

    // 비동기 GPT 호출
    CompletableFuture<GeneratedTexts> futureTexts = gptService.generateAdTextsAsync(
        request.getItemName(), request.getItemConcept(), request.getItemCategory(),
        request.getAddInformation(), imageUrl
    );

    // 결과 대기
    GeneratedTexts texts = futureTexts.join();

    Banner banner = new Banner();
    banner.setMainText1(texts.getMainText1());
    banner.setServText1(texts.getServText1());
    banner.setMainText2(texts.getMainText2());
    banner.setServText2(texts.getServText2());
    banner.setItemName(request.getItemName());
    banner.setItemConcept(request.getItemConcept());
    banner.setItemCategory(request.getItemCategory());
    banner.setPrompt(request.getAddInformation());
    banner.setImage(image);

    return bannerRepository.save(banner);
  }

  public Banner getBannerById(Long bannerId) {
    return bannerRepository.findById(bannerId)
        .orElseThrow(() -> new RuntimeException("배너를 찾을 수 없습니다."));
  }

  public Banner updateBanner(Long bannerId, BannerRequest request) {
    return bannerRepository.findById(bannerId).map(banner -> {
      Image image = imageRepository.findById(request.getImageId())
          .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));
      String imageUrl = image.getImageUrl();

      CompletableFuture<GeneratedTexts> futureTexts = gptService.generateAdTextsAsync(
          request.getItemName(), request.getItemConcept(), request.getItemCategory(),
          request.getAddInformation(), imageUrl
      );

      GeneratedTexts texts = futureTexts.join();

      banner.setMainText1(texts.getMainText1());
      banner.setServText1(texts.getServText1());
      banner.setMainText2(texts.getMainText2());
      banner.setServText2(texts.getServText2());
      banner.setItemName(request.getItemName());
      banner.setItemConcept(request.getItemConcept());
      banner.setItemCategory(request.getItemCategory());
      banner.setPrompt(request.getAddInformation());
      banner.setImage(image);

      return bannerRepository.save(banner);
    }).orElseThrow(() -> new RuntimeException("배너를 찾을 수 없습니다."));
  }


  public void deleteBanner(Long bannerId) {
    Banner banner = bannerRepository.findById(bannerId)
        .orElseThrow(() -> new RuntimeException("배너를 찾을 수 없습니다."));
    bannerRepository.delete(banner);
  }
}
