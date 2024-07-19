package com.tim_ohagan.commentservice.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Data
@Document(collection = "comments")
public class Comment {
    @Id
    private ObjectId id;
    private String postID;
    private String userID;
    private String username;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
}