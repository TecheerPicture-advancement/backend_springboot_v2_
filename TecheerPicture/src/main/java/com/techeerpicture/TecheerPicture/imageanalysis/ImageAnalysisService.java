package com.techeerpicture.TecheerPicture.imageanalysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class ImageAnalysisService {

    @Autowired
    private OpenAIVisionClient openAIVisionClient;

    @Autowired
    private ImageAnalysisRepository imageAnalysisRepository;

    public ImageAnalysis analyzeAndSaveImage(String imageUrl) throws IOException {
        // OpenAI Vision API 호출하여 이미지 분석
        String analysisText = openAIVisionClient.analyzeImage(imageUrl);

        // 데이터베이스에 저장
        ImageAnalysis imageAnalysis = new ImageAnalysis();
        imageAnalysis.setImageUrl(imageUrl);
        imageAnalysis.setAnalysisText(analysisText);
        return imageAnalysisRepository.save(imageAnalysis);
    }
}
