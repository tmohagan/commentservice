package com.tim_ohagan.commentservice.service;

import com.tim_ohagan.commentservice.model.SentimentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@Service
public class SentimentService {

    private static final String SENTIMENT_API_URL = "https://python-sentiment-kappa.vercel.app/analyze";

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<SentimentResponse> analyzeSentiment(String text) {
        return webClientBuilder.build()
            .post()
            .uri(SENTIMENT_API_URL)
            .bodyValue(Map.of("text", text))
            .retrieve()
            .bodyToMono(SentimentResponse.class)
            .onErrorResume(e -> Mono.error(new RuntimeException("Failed to analyze sentiment", e)));
    }
}