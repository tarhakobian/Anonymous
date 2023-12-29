package com.example.schoolarsanonymouspostingmodule.controller;

import com.example.schoolarsanonymouspostingmodule.service.CommentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/anonymous-posts/comment/{post_id}")
    public ResponseEntity<?> comment(@PathVariable Integer post_id, @NotNull String content) {
        commentService.comment(post_id, content);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/anonymous-posts/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/anonymous-posts/comment/{commentId}")
    public ResponseEntity<?> edit(@PathVariable Integer commentId, @NotNull @RequestParam String content) {
        commentService.edit(commentId, content);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
