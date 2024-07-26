package com.tim_ohagan.commentservice.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableReactiveMongoRepositories
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);
    private final String mongoUri = System.getenv("MONGO_URI");

    public MongoConfig() {
        logger.info("MongoDB URI: " + mongoUri);
    }

    @Override
    protected String getDatabaseName() {
        return "comment-service";
    }

    @Override
    @Bean
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(mongoUri);
    }
}