package com.tim_ohagan.commentservice.repository;

import com.tim_ohagan.commentservice.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostID(String postID);
}