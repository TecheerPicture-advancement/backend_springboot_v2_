package com.techeerpicture.TecheerPicture.Background;

import com.techeerpicture.TecheerPicture.Background.ImageTransform;
import com.techeerpicture.TecheerPicture.Image.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "backgrounds API", description = "AI 배경 이미지 생성 API")
@RestController
@RequestMapping("api/v1/backgrounds")
public class BackgroundController {

    private final BackgroundService backgroundService;
    private static final Logger log = LoggerFactory.getLogger(BackgroundController.class);

    @Autowired
    public BackgroundController(BackgroundService backgroundService) {
        this.backgroundService = backgroundService;
    }

    @Operation(summary = "AI 배경 이미지 생성", description = "입력된 정보를 바탕으로 AI 배경 이미지를 생성합니다.")
    @PostMapping
    public ResponseEntity<Background> generateBackground(@RequestBody BackgroundRequest request) {
        try {
            // Background 생성 및 저장
            Background createdBackground = backgroundService.createAndSaveBackground(
                    request.getImageId(),
                    request.getImageTransform(),
                    request.getScene(),
                    request.getPrompt()
            );
            return new ResponseEntity<>(createdBackground, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating background: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(summary = "AI 배경 이미지 조회", description = "특정 AI 배경 이미지 정보를 조회합니다.")
    @GetMapping("/{backgroundId}")
    public ResponseEntity<Background> getBackground(@PathVariable("backgroundId") Long backgroundId) {
        try {
            Background background = backgroundService.getBackgroundById(backgroundId);
            return new ResponseEntity<>(background, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving background: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "AI 배경 이미지 삭제", description = "특정 AI 배경 이미지를 삭제합니다.")
    @DeleteMapping("/{backgroundId}")
    public ResponseEntity<String> deleteBackground(@PathVariable("backgroundId") Long backgroundId) {
        try {
            backgroundService.deleteBackgroundById(backgroundId);
            return ResponseEntity.ok("Background with ID " + backgroundId + " has been deleted.");
        } catch (IllegalArgumentException e) {
            log.error("Error deleting background: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error deleting background: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
