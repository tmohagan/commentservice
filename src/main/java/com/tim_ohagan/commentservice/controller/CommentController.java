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

    @GetMapping("/{postID}")
    public ResponseEntity<List<Comment>> getCommentsByPostID(@PathVariable String postID) {
        try {
            return ResponseEntity.ok(commentService.getCommentsByPostID(postID));
        } catch (Exception e) {
            logger.error("Error fetching comments for postID: " + postID, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable String id, @RequestBody Comment comment) {
        ObjectId objectId = new ObjectId(id);
        return ResponseEntity.ok(commentService.updateComment(objectId, comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        String userID = requestBody.get("userID");
        ObjectId objectId = new ObjectId(id);
        commentService.deleteComment(objectId, userID);
        return ResponseEntity.ok().build();
    }
}
