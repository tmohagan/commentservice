package com.tim_ohagan.commentservice.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@Profile("!test")
public class MongoConfig extends AbstractMongoClientConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);
    private final String mongoUri = System.getProperty("MONGO_URI");

    public MongoConfig() {
        logger.info("MongoDB URI: " + mongoUri);
    }

    @Override
    protected String getDatabaseName() {
        return "comment-service";
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }
}