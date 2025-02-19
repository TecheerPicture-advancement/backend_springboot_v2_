package com.techeerpicture.TecheerPicture.instagramtext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.techeerpicture.TecheerPicture.Image.ImageRepository;

import java.util.Map;
import java.util.Optional;

@Tag(name = "instagram-texts API", description = "인스타 피드 문장 생성 API")
@RestController
@RequestMapping("/api/v1/instagram-texts")
public class InstagramTextController {

    @Autowired
    private GPTPidText gptPidText;

    @Autowired
    private InstagramTextService instagramTextService;

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
}
