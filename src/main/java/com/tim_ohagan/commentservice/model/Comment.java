package com.tim_ohagan.commentservice.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Data
@Document(collection = "comments")
public class Comment {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String postID;
    private String userID;
    private String username;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
}