package com.techeerpicture.TecheerPicture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

  @Bean("gptExecutor")
  public Executor gptExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    int coreCount = Runtime.getRuntime().availableProcessors();

    executor.setCorePoolSize(coreCount);
    executor.setMaxPoolSize(coreCount * 2);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("GPT-Async-");
    executor.initialize();
    return executor;
  }
}
