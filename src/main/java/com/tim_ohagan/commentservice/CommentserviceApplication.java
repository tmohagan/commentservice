package com.tim_ohagan.commentservice;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommentserviceApplication {

    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.load();
            System.setProperty("ALLOWED_ORIGINS", dotenv.get("ALLOWED_ORIGINS"));
        } catch (DotenvException e) {
            // Fallback to system environment variables
            String allowedOrigins = System.getenv("ALLOWED_ORIGINS");
            if (allowedOrigins != null) {
                System.setProperty("ALLOWED_ORIGINS", allowedOrigins);
            }
        }
        SpringApplication.run(CommentserviceApplication.class, args);
    }
}