package com.tim_ohagan.commentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class CommentserviceApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CommentserviceApplication.class);

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    public static void main(String[] args) {
        logger.info("Starting Comment Service Application");
        SpringApplication.run(CommentserviceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Verifying MongoDB connection...");
        try {
            String dbName = mongoTemplate.getMongoDatabase()
                    .flatMap(db -> Mono.just(db.getName()))
                    .block();
            logger.info("Successfully connected to MongoDB. Database name: {}", dbName);

            // Additional connection test
            mongoTemplate.collectionExists("comments")
                    .doOnSuccess(exists -> logger.info("'comments' collection exists: {}", exists))
                    .doOnError(error -> logger.error("Error checking 'comments' collection: ", error))
                    .block();

        } catch (Exception e) {
            logger.error("Failed to connect to MongoDB", e);
            // Optionally, you can throw an exception here to prevent the application from starting
            // throw new RuntimeException("Failed to connect to MongoDB", e);
        }

        // Log application environment
        logger.info("Application Environment:");
        logger.info("MONGO_URI: {}", System.getenv("MONGO_URI") != null ? "Set" : "Not Set");
        logger.info("ALLOWED_ORIGINS: {}", System.getenv("ALLOWED_ORIGINS"));
        logger.info("PORT: {}", System.getenv("PORT"));

        // You can add more startup logic here if needed
    }
}