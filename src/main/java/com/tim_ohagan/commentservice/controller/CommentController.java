package com.tim_ohagan.commentservice.controller;

import com.tim_ohagan.commentservice.model.Comment;
import com.tim_ohagan.commentservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        try {
            Comment createdComment = commentService.createComment(comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (Exception e) {
            logger.error("Error creating comment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{parentType}/{parentID}")
    public ResponseEntity<List<Comment>> getCommentsByParent(@PathVariable String parentType, @PathVariable String parentID) {
        try {
            List<Comment> comments = commentService.getCommentsByParent(parentID, parentType);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            logger.error("Error fetching comments for " + parentType + " with ID: " + parentID, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        try {
            String content = requestBody.get("content");
            String userID = requestBody.get("userID");
            
            Comment updatedComment = new Comment();
            updatedComment.setContent(content);
            
            Comment result = commentService.updateComment(id, updatedComment, userID);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            logger.error("Error updating comment", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        try {
            String userID = requestBody.get("userID");
            commentService.deleteComment(id, userID);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting comment", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}