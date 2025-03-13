package com.techeerpicture.TecheerPicture.Image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.techeerpicture.TecheerPicture.Image.entity.Image;
import com.techeerpicture.TecheerPicture.Image.repository.ImageRepository;
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
            .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다: ID = " + id));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String bucketName = "techeer-picture-bucket";
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public Image saveImage(String imageUrl) {
        Image image = new Image();
        image.setImageUrl(imageUrl);
        return imageRepository.save(image);
    }

    public void deleteImageById(Long imageId) {
        Image image = imageRepository.findById(imageId)
            .orElseThrow(() -> new RuntimeException("해당 ID로 이미지를 찾을 수 없습니다: " + imageId));

        deleteImageFromS3(image.getImageUrl());
        imageRepository.deleteById(imageId);
    }

    public void deleteImageFromS3(String imageUrl) {
        try {
            String bucketName = "techeer-picture-bucket";
            String objectKey = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            amazonS3.deleteObject(bucketName, objectKey);
        } catch (Exception e) {
            System.err.println("S3에서 이미지를 삭제할 수 없습니다: " + e.getMessage());
        }
    }
}
