package com.techeerpicture.TecheerPicture.Image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import com.techeerpicture.TecheerPicture.User.User; // User 클래스 임포트
import com.techeerpicture.TecheerPicture.User.UserRepository; // UserRepository 임포트
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import java.io.IOException;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Service
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3 amazonS3;

    // ImageRepository 의존성 추가
    private final ImageRepository imagerepository;
    private final UserRepository userRepository; // UserRepository 추가


    public Image getImageById(Long id) {
        return imagerepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found or does not belong to user"));
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

        }  catch (Exception e) {
            // 업로드 도중 실패 시, 이미 업로드된 파일 삭제
            if (uploadedImageUrl != null) {
                amazonS3.deleteObject("techeer-picture-bucket", uploadedImageUrl);
            }
            throw new IOException("Error uploading image: " + e.getMessage(), e);
        }
    }

    public Image saveImage(Long userId, String imageUrl) {
        // Image 객체 생성
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Image image = new Image();
        image.setUser(user); // 외래 키 설정
        image.setImageUrl(imageUrl); // imageUrl 설정


        // 데이터 저장
        return imagerepository.save(image);
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
