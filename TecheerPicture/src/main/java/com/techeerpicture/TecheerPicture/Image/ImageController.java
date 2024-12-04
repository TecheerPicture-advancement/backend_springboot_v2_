package com.techeerpicture.TecheerPicture.Image;

import java.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import com.techeerpicture.TecheerPicture.User.User;
import com.techeerpicture.TecheerPicture.User.UserRepository;


@Tag(name = "Image API", description = "Image Upload and Retrieval API")
@RestController
@RequestMapping("api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final UserRepository userRepository;

    @Operation(summary = "Upload an image", description = "Uploads an image to the server.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAndSaveImage(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("userId") Long userId) {
        String uploadedImageUrl = null;
        try {
            // S3에 이미지 업로드
            uploadedImageUrl = imageService.uploadImage(file);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            // 업로드된 URL과 userId를 DB에 저장
            imageService.saveImage(userId, uploadedImageUrl);

            return ResponseEntity.ok("Image uploaded and saved successfully. URL: " + uploadedImageUrl);

        } catch (RuntimeException e) {
            // 인증 실패: S3에 업로드된 이미지 삭제
            if (uploadedImageUrl != null) {
                imageService.deleteImageFromS3(uploadedImageUrl);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User authentication failed: " + e.getMessage());

        } catch (IOException e) {
            // S3 업로드 실패 시 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading image: " + e.getMessage());
        }
    }

    @Operation(summary = "Retrieve image details by ID")
    @GetMapping("/{Imageid}")
    public Image getImage(@PathVariable("Imageid") Long id) {
        return imageService.getImageById(id);
    }
}
