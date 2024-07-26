package com.tim_ohagan.commentservice.controller;

import com.tim_ohagan.commentservice.model.Comment;
import com.tim_ohagan.commentservice.service.CommentService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @GetMapping("/{parentType}/{parentID}")
    public Flux<Comment> getCommentsByParent(
            @PathVariable String parentID,
            @PathVariable String parentType) {
        logger.debug("Fetching comments for parentID: {} and parentType: {}", parentID, parentType);
        return commentService.getCommentsByParent(parentID, parentType)
                .doOnComplete(() -> logger.debug("Finished fetching comments for parentID: {} and parentType: {}", parentID, parentType));
    }

    @PostMapping("/{parentType}/{parentID}")
    public Mono<ResponseEntity<Comment>> createComment(
            @PathVariable String parentID,
            @PathVariable String parentType,
            @RequestBody Comment comment) {
                logger.debug("Received comment creation request. ParentType: {}, ParentID: {}, Comment: {}", parentType, parentID, comment);
                
                if (!ObjectId.isValid(parentID)) {
            logger.warn("Invalid parentID: {}", parentID);
            return Mono.just(ResponseEntity.badRequest().build());
        }
        logger.debug("Creating comment for parentID: {} and parentType: {}", parentID, parentType);
        comment.setParentID(new ObjectId(parentID));
        comment.setParentType(parentType);
        return commentService.createComment(comment)
                .map(savedComment -> {
                    logger.debug("Comment created successfully with ID: {}", savedComment.getId());
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
                })
                .onErrorResume(e -> {
                    logger.error("Error creating comment", e);
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @PutMapping("/{parentType}/{id}")
    public Mono<ResponseEntity<Comment>> updateComment(
            @PathVariable String id,
            @RequestBody Comment comment,
            @RequestHeader("User-ID") String userID) {
        logger.debug("Updating comment with ID: {} by user: {}", id, userID);
        return commentService.updateComment(id, comment, userID)
                .map(updatedComment -> {
                    logger.debug("Comment updated successfully: {}", updatedComment.getId());
                    return ResponseEntity.ok(updatedComment);
                })
                .onErrorResume(e -> {
                    if (e.getMessage().contains("not authorized")) {
                        logger.warn("Unauthorized attempt to update comment: {} by user: {}", id, userID);
                        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
                    }
                    logger.error("Error updating comment: {}", id, e);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    @DeleteMapping("/{parentType}/{id}")
    public Mono<ResponseEntity<Void>> deleteComment(
            @PathVariable String id,
            @RequestHeader("User-ID") String userID) {
        logger.debug("Deleting comment with ID: {} by user: {}", id, userID);
        return commentService.deleteComment(id, userID)
                .then(Mono.fromRunnable(() -> logger.debug("Comment deleted successfully: {}", id)))
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(e -> {
                    if (e.getMessage().contains("not authorized")) {
                        logger.warn("Unauthorized attempt to delete comment: {} by user: {}", id, userID);
                        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
                    }
                    logger.error("Error deleting comment: {}", id, e);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }
}