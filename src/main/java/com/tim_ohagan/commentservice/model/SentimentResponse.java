package com.tim_ohagan.commentservice.model;

import lombok.Data;

@Data
public class SentimentResponse {
    private double compound_score;
    private String sentiment;
    private String text;
}