package com.techeerpicture.TecheerPicture.Banner.service;

import com.techeerpicture.TecheerPicture.Banner.dto.BannerRequest;
import com.techeerpicture.TecheerPicture.Banner.entity.Banner;
import com.techeerpicture.TecheerPicture.Banner.external.GPTService;
import com.techeerpicture.TecheerPicture.Banner.repository.BannerRepository;
import com.techeerpicture.TecheerPicture.Banner.util.GeneratedTexts;
import com.techeerpicture.TecheerPicture.Image.entity.Image;
import com.techeerpicture.TecheerPicture.Image.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class BannerService {

  @Autowired
  private BannerRepository bannerRepository;

  @Autowired
  private GPTService gptService;

  @Autowired
  private ImageRepository imageRepository;

  @Autowired
  @Qualifier("gptExecutor")
  private Executor gptExecutor;

  public Banner createBanner(BannerRequest request) {
    Image image = imageRepository.findById(request.getImageId())
        .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));

    GeneratedTexts texts = gptService.generateAdTexts(
        request.getItemName(), request.getItemConcept(),
        request.getItemCategory(), request.getAddInformation(),
        image.getImageUrl()
    );

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

  public List<Banner> createBannersInParallel(List<BannerRequest> requests) {
    List<CompletableFuture<Banner>> futures = requests.stream()
        .map(request -> {
          Image image = imageRepository.findById(request.getImageId())
              .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));

          return gptService.generateAdTextsAsync(
              request.getItemName(), request.getItemConcept(),
              request.getItemCategory(), request.getAddInformation(),
              image.getImageUrl()
          ).thenApplyAsync(texts -> {
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
            return banner;
          }, gptExecutor);
        })
        .collect(Collectors.toList());

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    return futures.stream()
        .map(CompletableFuture::join)
        .map(bannerRepository::save)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Banner getBannerById(Long bannerId) {
    return bannerRepository.findById(bannerId)
        .orElseThrow(() -> new RuntimeException("배너를 찾을 수 없습니다."));
  }

  public Banner updateBanner(Long bannerId, BannerRequest request) {
    return bannerRepository.findById(bannerId).map(banner -> {
      Image image = imageRepository.findById(request.getImageId())
          .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));

      GeneratedTexts texts = gptService.generateAdTexts(
          request.getItemName(), request.getItemConcept(),
          request.getItemCategory(), request.getAddInformation(),
          image.getImageUrl()
      );

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
