package com.tim_ohagan.commentservice;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class CommentserviceApplication {

    private static final Logger logger = LoggerFactory.getLogger(CommentserviceApplication.class);

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    
    public static void main(String[] args) {
        logger.info("Starting Comment Service Application");
        try {
            Dotenv dotenv = Dotenv.load();
            System.setProperty("ALLOWED_ORIGINS", dotenv.get("ALLOWED_ORIGINS"));
            System.setProperty("MONGO_URI", dotenv.get("MONGO_URI"));
        } catch (DotenvException e) {
            // Fallback to system environment variables
            logger.warn("Failed to load .env file, falling back to system environment variables", e);
            String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
            if (allowedOrigins != null) {
                System.setProperty("ALLOWED_ORIGINS", allowedOrigins);
            }
            String mongoUri = System.getenv("MONGO_URI");
            if (mongoUri != null) {
                System.setProperty("MONGO_URI", mongoUri);
            }
        }
        
        SpringApplication.run(CommentserviceApplication.class, args);
        logger.info("Comment Service Application started successfully");
    }
}