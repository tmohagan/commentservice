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

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{parentType}/{parentID}")
    public Flux<Comment> getCommentsByParent(
            @PathVariable String parentID,
            @PathVariable String parentType) {
        return commentService.getCommentsByParent(parentID, parentType);
    }

    @PostMapping("/{parentType}/{parentID}")
    public Mono<ResponseEntity<Comment>> createComment(
            @PathVariable String parentID,
            @PathVariable String parentType,
            @RequestBody Comment comment) {
        if (!ObjectId.isValid(parentID)) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
        comment.setParentID(new ObjectId(parentID));
        comment.setParentType(parentType);
        return commentService.createComment(comment)
                .map(savedComment -> ResponseEntity.status(HttpStatus.CREATED).body(savedComment))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PutMapping("/{parentType}/{id}")
    public Mono<ResponseEntity<Comment>> updateComment(
            @PathVariable String id,
            @RequestBody Comment comment,
            @RequestHeader("User-ID") String userID) {
        return commentService.updateComment(id, comment, userID)
                .map(updatedComment -> ResponseEntity.ok(updatedComment))
                .onErrorResume(e -> {
                    if (e.getMessage().contains("not authorized")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
                    }
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    @DeleteMapping("/{parentType}/{id}")
    public Mono<ResponseEntity<Void>> deleteComment(
            @PathVariable String id,
            @RequestHeader("User-ID") String userID) {
        return commentService.deleteComment(id, userID)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(e -> {
                    if (e.getMessage().contains("not authorized")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
                    }
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }
}