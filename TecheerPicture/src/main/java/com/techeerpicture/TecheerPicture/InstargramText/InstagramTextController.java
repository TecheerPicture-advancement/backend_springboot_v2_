package com.techeerpicture.TecheerPicture.instagramtext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.techeerpicture.TecheerPicture.Image.ImageRepository;

import java.util.Map;
import java.util.Optional;

@Tag(name = "instagram-texts API", description = "인스타 피드 문장 CRUD API")
@RestController
@RequestMapping("/api/v1/instagram-texts")
public class InstagramTextController {

    @Autowired
    private GPTPidText gptPidText;

    @Autowired
    private InstagramTextService instagramTextService;

    @Autowired
    private InstagramTextRepository instagramTextRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Operation(summary = "Instagram 피드 문장 생성", description = "입력된 텍스트 프롬프트를 바탕으로 OpenAI API를 사용해 Instagram 광고 문구를 생성하고 저장합니다.")
    @PostMapping
    public ResponseEntity<Map<String, Object>> generateInstagramText(@RequestBody InstagramTextRequest request) {
        Long imageId = request.getImageId();
        String textPrompt = request.getTextPrompt();

        boolean imageExists = imageRepository.existsById(imageId);
        if (!imageExists) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", "이미지 ID가 존재하지 않습니다.",
                    "data", Map.of("image_id", imageId)
            ));
        }

        String generatedText = gptPidText.generateInstagramText(textPrompt);
        InstagramText instagramText = instagramTextService.saveGeneratedText(imageId, generatedText);

        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "Instagram 피드 문장 생성 및 저장 성공",
                "data", Map.of(
                        "id", instagramText.getId(),
                        "image_id", instagramText.getImageId(),
                        "instagramText", instagramText.getGeneratedText()
                )
        ));
    }

    @Operation(summary = "Instagram 피드 문장 조회", description = "특정 ID의 Instagram 피드 문장을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getInstagramText(@PathVariable Long id) {
        Optional<InstagramText> instagramText = instagramTextRepository.findById(id);

        if (instagramText.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 404,
                    "message", "Instagram 피드 문장을 찾을 수 없습니다.",
                    "data", Map.of("id", id)
            ));
        }

        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "Instagram 피드 문장 조회 성공",
                "data", Map.of(
                        "id", instagramText.get().getId(),
                        "image_id", instagramText.get().getImageId(),
                        "instagramText", instagramText.get().getGeneratedText()
                )
        ));
    }

    @Operation(summary = "Instagram 피드 문장 수정", description = "기존 Instagram 피드 문장을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateInstagramText(@PathVariable Long id, @RequestBody InstagramTextRequest request) {
        Optional<InstagramText> optionalText = instagramTextRepository.findById(id);

        if (optionalText.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 404,
                    "message", "Instagram 피드 문장을 찾을 수 없습니다.",
                    "data", Map.of("id", id)
            ));
        }

        InstagramText instagramText = optionalText.get();
        instagramText.setGeneratedText(request.getTextPrompt());
        instagramText.setUpdatedAt(java.time.LocalDateTime.now());
        instagramTextRepository.save(instagramText);

        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "Instagram 피드 문장 수정 성공",
                "data", Map.of(
                        "id", instagramText.getId(),
                        "image_id", instagramText.getImageId(),
                        "instagramText", instagramText.getGeneratedText()
                )
        ));
    }

    @Operation(summary = "Instagram 피드 문장 삭제", description = "Instagram 피드 문장을 완전히 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteInstagramText(@PathVariable Long id) {
        Optional<InstagramText> optionalText = instagramTextRepository.findById(id);

        if (optionalText.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 404,
                    "message", "Instagram 피드 문장을 찾을 수 없습니다.",
                    "data", Map.of("id", id)
            ));
        }

        instagramTextRepository.deleteById(id); // ✅ 데이터 완전 삭제

        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "Instagram 피드 문장 완전 삭제 성공",
                "data", Map.of("id", id)
        ));
    }
}
