package com.techeerpicture.TecheerPicture.Image;

import java.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Tag(name = "Image API", description = "이미지 업로드 API")
@RestController
@RequestMapping("api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "이미지 업로드", description = "서버에 이미지를 업로드합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAndSaveImage(@RequestParam("file") MultipartFile file) {
        try {
            // S3에 이미지 업로드
            String uploadedImageUrl = imageService.uploadImage(file);

            // 업로드된 URL을 DB에 저장하고, 저장된 이미지 ID를 반환
            Image savedImage = imageService.saveImage(uploadedImageUrl);
            Long imageId = savedImage.getId(); // 저장된 이미지 ID 가져오기

            return ResponseEntity.ok("이미지가 업로드 중입니다. 업로드가 완료되면 URL이 업데이트 됩니다: " +
                uploadedImageUrl + "\n이미지 ID: " + imageId);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("이미지 업로드 실패: " + e.getMessage());
        }
    }

    @Operation(summary = "이미지 조회", description = "이미지 ID를 이용해 업로드된 이미지를 조회합니다.")
    @GetMapping("/{imageId}")
    public ResponseEntity<Image> getImage(@PathVariable("imageId") Long imageId) {
        Image image = imageService.getImageById(imageId);
        if (image == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(image);
    }

    @Operation(summary = "이미지 삭제", description = "ID를 이용하여 서버와 DB에서 이미지를 삭제합니다.")
    @DeleteMapping("/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable("imageId") Long imageId) {
        try {
            // DB에서 이미지 정보 가져오기
            Image image = imageService.getImageById(imageId);
            if (image == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("이미지를 찾을 수 없습니다: ID = " + imageId);
            }

            // S3에서 이미지 삭제
            imageService.deleteImageFromS3(image.getImageUrl());

            // DB에서 이미지 정보 삭제
            imageService.deleteImageById(imageId);

            return ResponseEntity.ok("이미지가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("이미지 삭제 실패: " + e.getMessage());
        }
    }

    @ControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public ResponseEntity<String> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
            return ResponseEntity.badRequest().body("업로드된 파일 크기가 서버 허용 최대 크기를 초과했습니다.");
        }
    }
}
