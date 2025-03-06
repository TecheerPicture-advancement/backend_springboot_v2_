package com.techeerpicture.TecheerPicture.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class S3UploaderService {

  private final AmazonS3 s3Client;

  private String bucketName = "techeer-picture-bucket";

  public String uploadImageFromUrl(String imageUrl, String s3Key) {
    try {
      // 이미지 다운로드
      URL url = new URL(imageUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
      InputStream inputStream = connection.getInputStream();

      // 메타데이터 설정
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType("image/png");

      // S3에 업로드
      s3Client.putObject(bucketName, s3Key, inputStream, metadata);
      inputStream.close();

      // 업로드된 파일의 S3 URL 반환
      return s3Client.getUrl(bucketName, s3Key).toString();
    } catch (Exception e) {
      throw new RuntimeException("S3 업로드 실패: " + e.getMessage());
    }
  }
}
