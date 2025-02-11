package com.techeerpicture.TecheerPicture.Background;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeerpicture.TecheerPicture.Background.BackgroundRequest;
import com.techeerpicture.TecheerPicture.Background.PixelcutRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PixelcutService {

    @Value("${PIXELCUT_APIKEY}")
    private String pixelcutApiKey;

    private static final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PixelcutService pixelcutService;

    public String callPixelcutApi(BackgroundRequest request) throws Exception {
        // 1. Database에서 이미지 URL 가져오기
        String imageUrl = getImageUrlFromDatabase(request.getImageId());

        // 2. Pixelcut 요청 데이터 생성
        PixelcutRequest pixelcutRequest = new PixelcutRequest(
                imageUrl,
                request.getImageTransform(),
                request.getScene(),
                request.getPrompt(),
                null
        );

        // 3. 요청 바디 JSON 직렬화
        String requestBodyJson = objectMapper.writeValueAsString(pixelcutRequest);

        // 4. HTTP 요청 생성
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), requestBodyJson);

        Request httpRequest = new Request.Builder()
                .url("https://api.developer.pixelcut.ai/v1/generate-background")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("X-API-KEY", pixelcutApiKey)
                .build();

        // 5. HTTP 요청 실행 및 응답 처리
        try (Response response = client.newCall(httpRequest).execute()) {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    return responseBody.string();
                } else {
                    throw new RuntimeException("Pixelcut API 응답 본문이 null입니다.");
                }
            } else {
                throw new RuntimeException("Pixelcut API 호출 실패: " + response.message());
            }
        }
    }

    private String getImageUrlFromDatabase(Long imageId) {
        return "image-url-placeholder"; // 실제 DB 호출 로직으로 교체
    }
}
