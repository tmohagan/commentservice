package com.tim_ohagan.commentservice.controller;

import com.tim_ohagan.commentservice.model.Comment;
import com.tim_ohagan.commentservice.service.CommentService;
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

    @GetMapping
    public Flux<Comment> getCommentsByParent(
            @RequestParam String parentID,
            @RequestParam String parentType) {
        return commentService.getCommentsByParent(parentID, parentType);
    }

    @PostMapping
    public Mono<ResponseEntity<Comment>> createComment(@RequestBody Comment comment) {
        return commentService.createComment(comment)
                .map(savedComment -> ResponseEntity.status(HttpStatus.CREATED).body(savedComment))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PutMapping("/{id}")
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

    @DeleteMapping("/{id}")
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