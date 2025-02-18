package com.techeerpicture.TecheerPicture.config;

import ai.fal.client.FalClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FalClientConfig {

    @Bean
    public FalClient falClient() {
        return FalClient.withEnvCredentials();
    }
}
