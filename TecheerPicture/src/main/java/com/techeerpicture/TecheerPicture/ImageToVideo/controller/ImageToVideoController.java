package com.techeerpicture.TecheerPicture.ImageToVideo.controller;

import com.techeerpicture.TecheerPicture.ImageToVideo.service.ImageToVideoService;
import com.techeerpicture.TecheerPicture.ImageToVideo.dto.ImageToVideoRequest;
import com.techeerpicture.TecheerPicture.ImageToVideo.dto.ImageToVideoResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("api/v1")
@Tag(name = "ImageToVideo", description = "이미지를 비디오로 변환하는 API")
@RequiredArgsConstructor
public class ImageToVideoController {

    private final ImageToVideoService imageToVideoService;
    private static final Logger logger = LoggerFactory.getLogger(ImageToVideoController.class);

    @PostMapping("/imagetovideo")
    public ResponseEntity<?> submitVideoJob(@RequestBody ImageToVideoRequest request) {
        try {
            ImageToVideoResponse jobResult = imageToVideoService.submitVideoJob(request);
            return ResponseEntity.ok(jobResult);
        } catch (Exception e) {
            logger.error("API 요청 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류 발생: " + e.getMessage());
        }
    }
}
