package com.tim_ohagan.commentservice.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Override
    protected String getDatabaseName() {
        return "comment-service";
    }

    @Override
    @Bean
    public MongoClient reactiveMongoClient() {
        logger.debug("Attempting to create MongoClient with URI: {}", 
                     mongoUri.replaceAll(":[^/]+@", ":****@"));
        try {
            MongoClient client = MongoClients.create(mongoUri);
            logger.info("Successfully created MongoClient");
            return client;
        } catch (Exception e) {
            logger.error("Failed to create MongoClient", e);
            throw e;
        }
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        try {
            ReactiveMongoTemplate template = new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
            logger.info("Successfully created ReactiveMongoTemplate");
            return template;
        } catch (Exception e) {
            logger.error("Failed to create ReactiveMongoTemplate", e);
            throw e;
        }
    }
}