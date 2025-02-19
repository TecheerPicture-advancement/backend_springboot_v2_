package com.techeerpicture.TecheerPicture.ImageToVideo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageToVideoResponse {
    private VideoResponse video;
    private String finalPrompt;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VideoResponse {
        private String url;
        private String content_type;
        private String file_name;
        private int file_size;
    }
}