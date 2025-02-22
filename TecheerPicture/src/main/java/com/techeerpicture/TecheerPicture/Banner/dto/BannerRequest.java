package com.techeerpicture.TecheerPicture.Banner.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "광고 텍스트 생성 요청")
public class BannerRequest {

  @Schema(description = "제품 이름", example = "스킨케어")
  private String itemName;

  @Schema(description = "제품 컨셉", example = "촉촉한")
  private String itemConcept;

  @Schema(description = "제품 카테고리", example = "화장품")
  private String itemCategory;

  @Schema(description = "추가 정보", example = "3일간만 진행되는 이벤트")
  private String addInformation;

  @Schema(description = "이미지 ID", example = "1")
  private Long imageId;

  // Getter 및 Setter
  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public String getItemConcept() {
    return itemConcept;
  }

  public void setItemConcept(String itemConcept) {
    this.itemConcept = itemConcept;
  }

  public String getItemCategory() {
    return itemCategory;
  }

  public void setItemCategory(String itemCategory) {
    this.itemCategory = itemCategory;
  }

  public String getAddInformation() {
    return addInformation;
  }

  public void setAddInformation(String addInformation) {
    this.addInformation = addInformation;
  }

  public Long getImageId() {
    return imageId;
  }

  public void setImageId(Long imageId) {
    this.imageId = imageId;
  }
}
