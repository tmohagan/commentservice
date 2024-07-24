package com.tim_ohagan.commentservice.service;

import com.tim_ohagan.commentservice.model.Comment;
import com.tim_ohagan.commentservice.repository.CommentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getCommentsByParent(String parentID, String parentType) {
    try {
        ObjectId objectId = new ObjectId(parentID);
        return commentRepository.findByParentIDAndParentType(objectId.toHexString(), parentType);
    } catch (IllegalArgumentException e) {
        return Collections.emptyList();
    }
}

public Comment createComment(Comment comment) {
    try {
        ObjectId.isValid(comment.getParentID()); // Validate the ID
        comment.setCreatedAt(Instant.now());
        comment.setUpdatedAt(Instant.now());
        return commentRepository.save(comment);
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid parentID");
    }
}

    public Comment updateComment(ObjectId id, Comment updatedComment, String userID) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        if (!existingComment.getUserID().equals(userID)) {
            throw new RuntimeException("User not authorized to update this comment");
        }
        
        existingComment.setContent(updatedComment.getContent());
        existingComment.setUpdatedAt(Instant.now());
        
        return commentRepository.save(existingComment);
    }
    
    public void deleteComment(ObjectId id, String userID) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        if (!comment.getUserID().equals(userID)) {
            throw new RuntimeException("User not authorized to delete this comment");
        }
        
        commentRepository.deleteById(id);
    }
}