package com.tim_ohagan.commentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class CommentserviceApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("MONGO_URI", dotenv.get("MONGO_URI"));
        System.setProperty("ALLOWED_ORIGINS", dotenv.get("ALLOWED_ORIGINS"));
        SpringApplication.run(CommentserviceApplication.class, args);
    }
}