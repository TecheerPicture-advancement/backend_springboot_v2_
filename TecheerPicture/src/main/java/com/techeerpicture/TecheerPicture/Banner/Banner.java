package com.techeerpicture.TecheerPicture.Banner;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "banners")
public class Banner {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "image_id", nullable = false)
  private Long imageId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "main_text_1", nullable = false, length = 255)
  private String mainText1;

  @Column(name = "serv_text_1", nullable = false, length = 255)
  private String servText1;

  @Column(name = "main_text_2", nullable = false, length = 255)
  private String mainText2;

  @Column(name = "serv_text_2", nullable = false, length = 255)
  private String servText2;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
  // Getter and Setter methods

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getImageId() {
    return imageId;
  }

  public void setImageId(Long imageId) {
    this.imageId = imageId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
