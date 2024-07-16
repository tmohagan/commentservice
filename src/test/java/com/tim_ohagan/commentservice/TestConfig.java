package com.tim_ohagan.commentservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class TestConfig {

    @PostConstruct
    public void loadEnv() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("ALLOWED_ORIGINS", dotenv.get("ALLOWED_ORIGINS"));
        // Load other environment variables as needed
    }
}