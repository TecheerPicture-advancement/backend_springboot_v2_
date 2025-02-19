package com.techeerpicture.TecheerPicture.instagramtext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class InstagramTextService {

    @Autowired
    private InstagramTextRepository instagramTextRepository;

    public InstagramText saveGeneratedText(Long imageId, String generatedText) {
        InstagramText instagramText = new InstagramText();
        instagramText.setImageId(imageId);
        instagramText.setGeneratedText(generatedText);
        instagramText.setCreatedAt(LocalDateTime.now());
        instagramText.setUpdatedAt(LocalDateTime.now());

        return instagramTextRepository.save(instagramText);
    }
}
