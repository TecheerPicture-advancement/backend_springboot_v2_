package com.techeerpicture.TecheerPicture.Image.controller;

import com.techeerpicture.TecheerPicture.Image.dto.ImageResponse;
import com.techeerpicture.TecheerPicture.Image.entity.Image;
import com.techeerpicture.TecheerPicture.Image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

@Tag(name = "Image API", description = "이미지 업로드 API")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "이미지 업로드", description = "서버에 이미지를 업로드합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponse> uploadAndSaveImage(@RequestParam("file") MultipartFile file) {
        try {
            String uploadedImageUrl = imageService.uploadImage(file);
            Image savedImage = imageService.saveImage(uploadedImageUrl);
            ImageResponse response = new ImageResponse(uploadedImageUrl, savedImage.getId());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }

    @Operation(summary = "이미지 조회", description = "이미지 ID를 이용해 업로드된 이미지를 조회합니다.")
    @GetMapping("/{imageId}")
    public ResponseEntity<Image> getImage(@PathVariable("imageId") Long imageId) {
        Image image = imageService.getImageById(imageId);
        return ResponseEntity.ok(image);
    }

    @Operation(summary = "이미지 삭제", description = "ID를 이용하여 서버와 DB에서 이미지를 삭제합니다.")
    @DeleteMapping("/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable("imageId") Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            imageService.deleteImageFromS3(image.getImageUrl());
            imageService.deleteImageById(imageId);
            return ResponseEntity.ok("이미지가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("이미지 삭제 실패: " + e.getMessage());
        }
    }
}
