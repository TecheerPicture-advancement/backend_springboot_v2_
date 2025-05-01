package com.techeerpicture.TecheerPicture.InstagramText.controller;

import com.techeerpicture.TecheerPicture.InstagramText.entity.InstagramText;
import com.techeerpicture.TecheerPicture.InstagramText.service.GPTPidText;
import com.techeerpicture.TecheerPicture.InstagramText.service.InstagramTextService;
import com.techeerpicture.TecheerPicture.InstagramText.repository.InstagramTextRepository;
import com.techeerpicture.TecheerPicture.InstagramText.dto.InstagramTextRequest;
import com.techeerpicture.TecheerPicture.InstagramText.dto.InstagramTextResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "instagram-texts API", description = "인스타 피드 문장 CRUD API")
@RestController
@RequestMapping("/api/v1/instagram-texts")
@CrossOrigin(origins = {"http://localhost:3000", "https://252f-211-201-196-64.ngrok-free.app"})
public class InstagramTextController {

    private static final Logger logger = LoggerFactory.getLogger(InstagramTextController.class);

    @Autowired
    private InstagramTextService instagramTextService;

    @Autowired
    private InstagramTextRepository instagramTextRepository;

    @Autowired
    private GPTPidText gptPidText;

    @PostMapping
    public ResponseEntity<Map<String, Object>> generateInstagramText(
            @RequestParam String userId,
            @RequestParam String accessToken,
            @RequestBody InstagramTextRequest request) {

        Long imageId = request.getImageId();
        String textPrompt = request.getTextPrompt();

        RestTemplate restTemplate = new RestTemplate();
        String mediaUrl = "http://localhost:8080/api/v1/instagram/media?user_id=" + userId + "&access_token=" + accessToken;

        ResponseEntity<List> mediaResponse = restTemplate.getForEntity(mediaUrl, List.class);
        List<Map<String, Object>> mediaList = mediaResponse.getBody();

        List<String> captions = new ArrayList<>();
        if (mediaList != null) {
            for (Map<String, Object> media : mediaList) {
                if (media.containsKey("caption")) {
                    captions.add((String) media.get("caption"));
                }
            }
        }

        if (captions.size() > 10) {
            captions = captions.subList(0, 10);
        }

        logger.info("Instagram에서 가져온 최신 10개 caption 리스트: {}", captions);

        String captionExamples = String.join("\n", captions);
        String fullPrompt = "Write an engaging Instagram caption in a similar style to these previous captions:\n" +
                captionExamples + "\n\nUser Prompt: " + textPrompt;

        String generatedText = gptPidText.analyzeImageAndGenerateText(fullPrompt);
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

        instagramTextRepository.deleteById(id);

        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "Instagram 피드 문장 완전 삭제 성공",
                "data", Map.of("id", id)
        ));
    }
}
