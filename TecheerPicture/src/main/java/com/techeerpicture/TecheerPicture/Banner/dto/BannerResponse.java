package com.techeerpicture.TecheerPicture.Banner.dto;

public class BannerResponse {

  private Long id;
  private String message;
  private String mainText1;
  private String servText1;
  private String mainText2;
  private String servText2;
  private Long imageId;  // Image ID만 사용

  // 생성자
  public BannerResponse(Long id, String message, String mainText1, String servText1, String mainText2, String servText2, Long imageId) {
    this.id = id;
    this.message = message;
    this.mainText1 = mainText1;
    this.servText1 = servText1;
    this.mainText2 = mainText2;
    this.servText2 = servText2;
    this.imageId = imageId;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMainText1() {
    return mainText1;
  }

  public void setMainText1(String mainText1) {
    this.mainText1 = mainText1;
  }

  public String getServText1() {
    return servText1;
  }

  public void setServText1(String servText1) {
    this.servText1 = servText1;
  }

  public String getMainText2() {
    return mainText2;
  }

  public void setMainText2(String mainText2) {
    this.mainText2 = mainText2;
  }

  public String getServText2() {
    return servText2;
  }

  public void setServText2(String servText2) {
    this.servText2 = servText2;
  }

  public Long getImageId() {
    return imageId;
  }

  public void setImageId(Long imageId) {
    this.imageId = imageId;
  }
}
