package com.tim_ohagan.commentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class CommentserviceApplication {

    private static final Logger logger = LoggerFactory.getLogger(CommentserviceApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Comment Service Application");
        try {
            String mongoUri = System.getenv("MONGO_URI");
            if (mongoUri == null || mongoUri.isEmpty()) {
                logger.error("MONGO_URI environment variable is not set");
            } else {
                logger.info("MONGO_URI is set (not showing for security reasons)");
            }
            
            String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
            if (allowedOrigins == null || allowedOrigins.isEmpty()) {
                logger.error("ALLOWED_ORIGINS environment variable is not set");
            } else {
                logger.info("ALLOWED_ORIGINS: " + allowedOrigins);
            }
            
            SpringApplication.run(CommentserviceApplication.class, args);
            logger.info("Comment Service Application started successfully");
        } catch (Exception e) {
            logger.error("Error starting Comment Service Application", e);
        }
    }
}