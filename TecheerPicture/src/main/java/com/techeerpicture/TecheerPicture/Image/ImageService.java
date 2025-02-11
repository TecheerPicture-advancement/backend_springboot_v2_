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
    private final ImageRepository imageRepository;

    public Image getImageById(Long id) {
        return imageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Image not found"));
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
            // 업로드 도중 실패 시, 이미 업로드된 파일 삭제
            if (uploadedImageUrl != null) {
                amazonS3.deleteObject("techeer-picture-bucket", uploadedImageUrl);
            }
            throw new IOException("Error uploading image: " + e.getMessage(), e);
        }
    }

    public Image saveImage(String imageUrl) {
        // User 없이 Image 저장
        Image image = new Image();
        image.setImageUrl(imageUrl); // imageUrl 설정

        // 데이터 저장
        return imageRepository.save(image);
    }

    public void deleteImageFromS3(String imageUrl) {
        try {
            // S3 버킷 이름
            String bucketName = "techeer-picture-bucket";

            // S3에서 객체 키 추출 (URL에서 파일 이름만 가져오기)
            String objectKey = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            // S3에서 파일 삭제
            amazonS3.deleteObject(bucketName, objectKey);
        } catch (Exception e) {
            System.err.println("Error deleting image from S3: " + e.getMessage());
        }
    }
}
