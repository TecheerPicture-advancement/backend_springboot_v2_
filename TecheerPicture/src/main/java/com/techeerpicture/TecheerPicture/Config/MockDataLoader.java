package com.techeerpicture.TecheerPicture.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeerpicture.TecheerPicture.Background.Background;
import com.techeerpicture.TecheerPicture.Background.BackgroundRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class MockDataLoader implements CommandLineRunner {

  private final BackgroundRepository backgroundRepository;

  public MockDataLoader(BackgroundRepository backgroundRepository) {
    this.backgroundRepository = backgroundRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();

    // ✅ InputStream을 사용하여 JSON 파일을 로드
    InputStream inputStream = new ClassPathResource("mockdata/background_data.json").getInputStream();

    List<Background> backgrounds = objectMapper.readValue(inputStream, new TypeReference<List<Background>>() {});

    backgroundRepository.saveAll(backgrounds);
    System.out.println("✅ JSON 데이터가 DB에 삽입되었습니다.");
  }
}
