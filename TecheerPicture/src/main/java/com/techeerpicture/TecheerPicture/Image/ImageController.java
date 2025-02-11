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

@Tag(name = "Image API", description = "Image Upload and Retrieval API")
@RestController
@RequestMapping("api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Upload an image", description = "Uploads an image to the server.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAndSaveImage(@RequestParam("file") MultipartFile file) {
        String uploadedImageUrl = null;
        try {
            // S3에 이미지 업로드
            uploadedImageUrl = imageService.uploadImage(file);

            // 업로드된 URL을 DB에 저장
            imageService.saveImage(uploadedImageUrl);

            return ResponseEntity.ok("Image uploaded and saved successfully. URL: " + uploadedImageUrl);

        } catch (IOException e) {
            // S3 업로드 실패 시 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error uploading image: " + e.getMessage());
        }
    }

    @Operation(summary = "Retrieve image details by ID")
    @GetMapping("/{imageId}")
    public Image getImage(@PathVariable("imageId") Long id) {
        return imageService.getImageById(id);
    }
}
