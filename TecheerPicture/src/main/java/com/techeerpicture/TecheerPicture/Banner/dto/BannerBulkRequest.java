package com.techeerpicture.TecheerPicture.Banner.dto;

import java.util.List;

public class BannerBulkRequest {
  private List<BannerRequest> requests;

  public List<BannerRequest> getRequests() {
    return requests;
  }

  public void setRequests(List<BannerRequest> requests) {
    this.requests = requests;
  }
}
