package com.techeerpicture.TecheerPicture.imageanalysis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Tag(name = "image-analysis API", description = "이미지 분석 데이터 관리 API")
@RestController
@RequestMapping("/api/v1/image-analyses")
public class ImageAnalysisController {

    @Autowired
    private ImageAnalysisService imageAnalysisService;

    @Autowired
    private ImageAnalysisRepository imageAnalysisRepository;

    @Operation(summary = "이미지 분석 및 저장", description = "이미지 URL을 분석하고 결과를 데이터베이스에 저장합니다.")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createImageAnalysis(@RequestBody ImageAnalysisRequest request) {
        try {
            ImageAnalysis analysis = imageAnalysisService.analyzeAndSaveImage(request.getImageUrl());

            return ResponseEntity.ok(Map.of(
                    "code", 201,
                    "message", "이미지 분석 성공",
                    "data", Map.of(
                            "id", analysis.getId(),
                            "image_url", analysis.getImageUrl(),
                            "analysis_text", analysis.getAnalysisText()
                    )
            ));
        } catch (IOException e) {
            if (e.getMessage().contains("404")) {
                return ResponseEntity.status(404).body(Map.of(
                        "code", 404,
                        "message", "해당하는 리소스를 찾을 수 없습니다.",
                        "error", e.getMessage()
                ));
            }

            return ResponseEntity.internalServerError().body(Map.of(
                    "code", 500,
                    "message", "이미지 분석 중 오류 발생",
                    "error", e.getMessage()
            ));
        }
    }


    @Operation(summary = "이미지 분석 결과 조회", description = "특정 ID의 이미지 분석 데이터를 조회합니다.")
    @GetMapping("/{imganalysisid}")
    public ResponseEntity<Map<String, Object>> getImageAnalysis(@PathVariable("imganalysisid") Long id) {
        Optional<ImageAnalysis> analysis = imageAnalysisRepository.findById(id);

        if (analysis.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 404,
                    "message", "해당 아이디의 리소스를  찾을 수 없습니다.",
                    "data", Map.of("id", id)
            ));
        }

        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "이미지 분석 문장  조회 성공",
                "data", Map.of(
                        "id", analysis.get().getId(),
                        "image_url", analysis.get().getImageUrl(),
                        "analysis_text", analysis.get().getAnalysisText()
                )
        ));
    }

    @Operation(summary = "이미지 분석 결과 수정", description = "기존 이미지 분석 데이터를 수정합니다.")
    @PutMapping("/{imganalysisid}")
    public ResponseEntity<Map<String, Object>> updateImageAnalysis(@PathVariable("imganalysisid") Long id, @RequestBody ImageAnalysisUpdateRequest request) {
        Optional<ImageAnalysis> optionalAnalysis = imageAnalysisRepository.findById(id);

        if (optionalAnalysis.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 404,
                    "message", "해당 id의리소스를 찾을 수 없습니다.",
                    "data", Map.of("id", id)
            ));
        }

        ImageAnalysis analysis = optionalAnalysis.get();
        analysis.setAnalysisText(request.getAnalysisText()); // 분석 문장만 수정 가능
        analysis.setUpdatedAt(java.time.LocalDateTime.now());
        imageAnalysisRepository.save(analysis);

        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "이미지 분석 문장 수정 성공",
                "data", Map.of(
                        "id", analysis.getId(),
                        "image_url", analysis.getImageUrl(),
                        "analysis_text", analysis.getAnalysisText()
                )
        ));
    }

    @Operation(summary = "이미지 분석 데이터 삭제", description = "이미지 분석 데이터를 완전히 삭제합니다.")
    @DeleteMapping("/{imganalysisid}")
    public ResponseEntity<Map<String, Object>> deleteImageAnalysis(@PathVariable("imganalysisid") Long id) {
        Optional<ImageAnalysis> optionalAnalysis = imageAnalysisRepository.findById(id);

        if (optionalAnalysis.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 404,
                    "message", "해당 id의리소스를 찾을 수 없습니다.",
                    "data", Map.of("id", id)
            ));
        }

        imageAnalysisRepository.deleteById(id); //데이터 완전 삭제

        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "이미지 분석 문장 삭제 성공",
                "data", Map.of("id", id)
        ));
    }
}
