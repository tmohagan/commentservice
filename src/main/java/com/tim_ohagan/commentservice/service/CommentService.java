package com.tim_ohagan.commentservice.service;

import com.tim_ohagan.commentservice.model.Comment;
import com.tim_ohagan.commentservice.repository.CommentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SentimentService sentimentService;

    public Flux<Comment> getCommentsByParent(String parentID, String parentType) {
        if (!ObjectId.isValid(parentID)) {
            return Flux.empty();
        }
        return commentRepository.findByParentIDAndParentType(parentID, parentType);
    }

    public Mono<Comment> createComment(Comment comment) {
        if (!ObjectId.isValid(comment.getParentID())) {
            return Mono.error(new IllegalArgumentException("Invalid parentID"));
        }
        
        comment.setCreatedAt(Instant.now());
        comment.setUpdatedAt(Instant.now());
        
        return sentimentService.analyzeSentiment(comment.getContent())
            .flatMap(sentimentResult -> {
                comment.setSentimentScore(sentimentResult.getCompound_score());
                comment.setSentiment(sentimentResult.getSentiment());
                return commentRepository.save(comment);
            });
    }

    public Mono<Comment> updateComment(String id, Comment updatedComment, String userID) {
        if (!ObjectId.isValid(id)) {
            return Mono.error(new RuntimeException("Invalid comment ID"));
        }

        ObjectId objectId = new ObjectId(id);
        return commentRepository.findById(objectId)
            .switchIfEmpty(Mono.error(new RuntimeException("Comment not found")))
            .flatMap(existingComment -> {
                if (!existingComment.getUserID().equals(userID)) {
                    return Mono.error(new RuntimeException("User not authorized to update this comment"));
                }
                
                existingComment.setContent(updatedComment.getContent());
                existingComment.setUpdatedAt(Instant.now());
                
                return sentimentService.analyzeSentiment(existingComment.getContent())
                    .flatMap(sentimentResult -> {
                        existingComment.setSentimentScore(sentimentResult.getCompound_score());
                        existingComment.setSentiment(sentimentResult.getSentiment());
                        return commentRepository.save(existingComment);
                    });
            });
    }

    public Mono<Void> deleteComment(String id, String userID) {
        if (!ObjectId.isValid(id)) {
            return Mono.error(new RuntimeException("Invalid comment ID"));
        }

        ObjectId objectId = new ObjectId(id);
        return commentRepository.findById(objectId)
            .switchIfEmpty(Mono.error(new RuntimeException("Comment not found")))
            .flatMap(comment -> {
                if (!comment.getUserID().equals(userID)) {
                    return Mono.error(new RuntimeException("User not authorized to delete this comment"));
                }
                return commentRepository.delete(comment);
            });
    }
}