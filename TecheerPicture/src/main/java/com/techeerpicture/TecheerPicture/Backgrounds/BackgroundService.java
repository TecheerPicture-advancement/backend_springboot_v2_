package com.techeerpicture.TecheerPicture.Background;

import com.techeerpicture.TecheerPicture.Background.ImageTransform;
import com.techeerpicture.TecheerPicture.Image.ImageService;
import com.techeerpicture.TecheerPicture.Image.Image;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BackgroundService {

    private final BackgroundRepository backgroundRepository;
    private final PixelcutService pixelcutService;
    private final ImageRepository imageRepository;

    public BackgroundService(BackgroundRepository backgroundRepository, PixelcutService pixelcutService) {
        this.backgroundRepository = backgroundRepository;
        this.pixelcutService = pixelcutService;
        this.imageRepository = imageRepository;
    }

    // Background 생성 및 외부 API 호출
    @Transactional
    public String createAndSaveBackground(Long userId, BackgroundRequest request) {
        try {
            // 1. 외부 API 호출 (Pixelcut API)
            String response = pixelcutService.callPixelcutApi(request);

            // 2. 이미지 ID로 Image 엔티티 조회
            Long imageId = request.getImageId(); // 외래 키로 사용할 이미지 ID
            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Image not found for ID: " + imageId));

            // 3. API 응답 결과 처리 (응답 데이터를 활용하여 Background 객체 생성)
            Background background = new Background();
            background.setImageUrl(request.getImageId()); // 이미지 ID로부터 URL을 설정 (DB에서 가져오는 로직 포함)
            background.setScale(request.getImageTransform().getScale());
            background.setXCenter(request.getImageTransform().getX_center());
            background.setYCenter(request.getImageTransform().getY_center());
            background.setScene(request.getScene());
            background.setPrompt(request.getPrompt());

            // 4. Background 객체를 DB에 저장
            backgroundRepository.save(background);

            return "Background created successfully! API Response: " + response;
        } catch (Exception e) {
            throw new RuntimeException("Error while calling Pixelcut API: " + e.getMessage(), e);
        }
    }

    // Background 조회
    public Background getBackgroundById(Long id) {
        return backgroundRepository.findById(id).orElseThrow(() -> new RuntimeException("Background not found for ID: " + id));
    }

    // Background 삭제
    @Transactional
    public void deleteBackgroundById(Long backgroundId) {
        Background background = backgroundRepository.findById(backgroundId)
                .orElseThrow(() -> new IllegalArgumentException("Background with ID " + backgroundId + " not found."));
        backgroundRepository.delete(background);
    }
}