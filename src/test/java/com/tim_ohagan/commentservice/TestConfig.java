package com.tim_ohagan.commentservice;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class TestConfig {

    private static final Logger logger = LoggerFactory.getLogger(TestConfig.class);

    @PostConstruct
    public void loadEnv() {
        try {
            Dotenv dotenv = Dotenv.load();
            System.setProperty("ALLOWED_ORIGINS", dotenv.get("ALLOWED_ORIGINS"));
            // Load other environment variables as needed
        } catch (DotenvException e) {
            logger.warn("No .env file found. Using system environment variables.");
            // Fallback to system environment variables
            String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
            if (allowedOrigins != null) {
                System.setProperty("ALLOWED_ORIGINS", allowedOrigins);
            }
        }
    }
}