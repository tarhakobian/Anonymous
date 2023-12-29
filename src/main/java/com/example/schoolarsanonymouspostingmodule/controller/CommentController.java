package com.example.schoolarsanonymouspostingmodule.controller;

import com.example.schoolarsanonymouspostingmodule.model.dto.request.CommentRequest;
import com.example.schoolarsanonymouspostingmodule.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/anonymous-posts/comment")
    public ResponseEntity<?> comment(@RequestBody @Valid CommentRequest request) {
        commentService.comment(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/anonymous-posts/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/anonymous-posts/comment/{commentId}")
    public ResponseEntity<?> edit(@PathVariable Integer commentId, @NotNull @RequestParam String content) {
        commentService.edit(commentId, content);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
