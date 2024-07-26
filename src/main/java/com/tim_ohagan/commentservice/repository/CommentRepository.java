package com.tim_ohagan.commentservice.repository;

import com.tim_ohagan.commentservice.model.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CommentRepository extends ReactiveMongoRepository<Comment, ObjectId> {
    Flux<Comment> findByParentIDAndParentType(ObjectId parentID, String parentType);
}