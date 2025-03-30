package com.techeerpicture.TecheerPicture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync  // 비동기 처리 활성화
@ComponentScan(basePackages = "com.techeerpicture")
public class TecheerPictureApplication {
	public static void main(String[] args) {
		SpringApplication.run(TecheerPictureApplication.class, args);
	}
}
