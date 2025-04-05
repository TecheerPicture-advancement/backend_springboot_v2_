package com.techeerpicture.TecheerPicture.Banner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class BannerBulkRequest {

  @JsonProperty("requests")
  private List<BannerRequest> requests;

  public List<BannerRequest> getRequests() { return requests; }
  public void setRequests(List<BannerRequest> requests) { this.requests = requests; }
}
