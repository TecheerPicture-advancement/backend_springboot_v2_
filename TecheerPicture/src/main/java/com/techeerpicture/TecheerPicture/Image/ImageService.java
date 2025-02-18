package com.techeerpicture.TecheerPicture.Image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3 amazonS3;
    private final ImageRepository imagerepository;

    public Image getImageById(Long id) {
        return imagerepository.findById(id)
            .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다: ID = " + id));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String uploadedImageUrl = null;
        try {
            String bucketName = "techeer-picture-bucket"; // S3 버킷 이름
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename(); // 고유 파일 이름 생성

            // 파일 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // S3에 파일 업로드
            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);

            // 업로드된 파일의 URL 반환
            uploadedImageUrl = amazonS3.getUrl(bucketName, fileName).toString();
            return uploadedImageUrl;

        } catch (Exception e) {
            if (uploadedImageUrl != null) {
                amazonS3.deleteObject("techeer-picture-bucket", uploadedImageUrl);
            }
            throw new IOException("이미지 업로드 오류: " + e.getMessage(), e);
        }
    }

    public Image saveImage(String imageUrl) {
        Image image = new Image();
        image.setImageUrl(imageUrl);
        return imagerepository.save(image); // 저장된 Image 객체 반환
    }

    public void deleteImageById(Long imageId) {
        Image image = imagerepository.findById(imageId)
            .orElseThrow(() -> new RuntimeException("해당 ID로 이미지를 찾을 수 없습니다: " + imageId));

        // S3에서 이미지 삭제
        deleteImageFromS3(image.getImageUrl());

        // 데이터베이스에서 이미지 삭제
        imagerepository.deleteById(imageId);
    }

    public void deleteImageFromS3(String imageUrl) {
        try {
            String bucketName = "techeer-picture-bucket";
            String objectKey = extractKeyFromS3Url(imageUrl);
            amazonS3.deleteObject(bucketName, objectKey);
        } catch (Exception e) {
            System.err.println("S3에서 이미지를 삭제할 수 없습니다: " + e.getMessage());
        }
    }

    private String extractKeyFromS3Url(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }
}
