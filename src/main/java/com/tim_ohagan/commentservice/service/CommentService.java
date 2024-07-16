package com.tim_ohagan.commentservice.service;

import com.tim_ohagan.commentservice.model.Comment;
import com.tim_ohagan.commentservice.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getCommentsByPostID(String postID) {
        return commentRepository.findByPostID(postID);
    }

    public Comment createComment(Comment comment) {
        comment.setCreatedAt(Instant.now());
        comment.setUpdatedAt(Instant.now());
        return commentRepository.save(comment);
    }

    public Comment updateComment(String id, Comment updatedComment) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        existingComment.setContent(updatedComment.getContent());
        existingComment.setUpdatedAt(Instant.now());
        
        return commentRepository.save(existingComment);
    }

    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }
}