package com.techeerpicture.TecheerPicture.Background;

import com.techeerpicture.TecheerPicture.Background.ImageTransform;
import com.techeerpicture.TecheerPicture.Background.BackgroundResponse;
import com.techeerpicture.TecheerPicture.Image.entity.Image;
import com.techeerpicture.TecheerPicture.Image.repository.ImageRepository;
import com.techeerpicture.TecheerPicture.Image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Tag(name = "backgrounds API", description = "AI 배경 이미지 생성 API")

@RestController
@RequestMapping("/api/v1/background")
public class BackgroundController {

    private final BackgroundService backgroundService;
    private final ImageRepository imageRepository;

    @Autowired
    public BackgroundController(BackgroundService backgroundService, ImageRepository imageRepository) {
        this.backgroundService = backgroundService;
        this.imageRepository = imageRepository;
    }

    @Operation(summary = "AI 배경 이미지 생성", description = "입력된 정보를 바탕으로 AI 배경 이미지를 생성합니다.")
    /**
     * 새로운 배경 생성 요청 처리.
     * @param backgroundRequest 사용자 요청 데이터.
     * @return 생성된 Background 엔티티.
     */
    @PostMapping
    public ResponseEntity<BackgroundResponse> generateBackground(@RequestBody BackgroundRequest backgroundRequest) {
        String imageUrl = imageRepository.findById(backgroundRequest.getImageId())
            .map(Image::getImageUrl)
            .orElseThrow(() -> new IllegalArgumentException("Invalid imageId: " + backgroundRequest.getImageId()));

        // 새로운 BackgroundRequest 객체 생성
        BackgroundRequest processedRequest = new BackgroundRequest();
        processedRequest.setImageId(backgroundRequest.getImageId());
        processedRequest.setImageTransform(backgroundRequest.getImageTransform());
        processedRequest.setScene(backgroundRequest.getScene());
        processedRequest.setPrompt(backgroundRequest.getPrompt());
        processedRequest.setNegativePrompt(null); // 서버에서 처리

        Background background = backgroundService.createAndSaveBackground(processedRequest);
        BackgroundResponse response = new BackgroundResponse(
            background.getId(),
            background.getImageUrl(),
            background.getScene(),
            background.getPrompt()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "AI 배경 이미지 조회", description = "특정 AI 배경 이미지 정보를 조회합니다.")
    @GetMapping("/{backgroundId}")
    public ResponseEntity<Background> getBackgroundById(@PathVariable Long backgroundId) {
        Background background = backgroundService.getBackgroundById(backgroundId);
        return ResponseEntity.ok(background);
    }

    @Operation(summary = "AI 배경 이미지 삭제", description = "특정 AI 배경 이미지를 완전히 삭제합니다.")
    @DeleteMapping("/{backgroundId}")
    public ResponseEntity<?> deleteBackground(@PathVariable Long backgroundId) {
        try {
            backgroundService.deleteBackground(backgroundId);
            return ResponseEntity.ok("배경 이미지가 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 ID의 Background를 찾을 수 없습니다: " + backgroundId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("배경 이미지 삭제 중 오류 발생");
        }
    }

}
