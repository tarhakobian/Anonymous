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

/**
 * Controller class for managing comments on anonymous posts.
 * <p>
 * Author: Taron Hakobyan
 */
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CommentController {

    private final CommentService commentService;

    /**
     * Creates a new comment on an anonymous post.
     *
     * @param request The request body containing the comment details.
     * @return ResponseEntity with status 201 Created.
     */
    @PostMapping("/anonymous-posts/comment")
    public ResponseEntity<?> comment(@RequestBody @Valid CommentRequest request) {
        Integer commentId = commentService.comment(request);
        return new ResponseEntity<>(commentId, HttpStatus.CREATED);
    }

    /**
     * Deletes a comment by its identifier.
     *
     * @param commentId Identifier of the comment to be deleted.
     * @return ResponseEntity with status 200 OK.
     */
    @DeleteMapping("/anonymous-posts/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Edits the content of a comment by its identifier.
     *
     * @param commentId Identifier of the comment to be edited.
     * @param content   New content for the comment.
     * @return ResponseEntity with status 200 OK.
     */
    @PatchMapping("/anonymous-posts/comment/{commentId}")
    public ResponseEntity<?> edit(@PathVariable Integer commentId, @NotNull @RequestParam String content) {
        commentService.edit(commentId, content);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Likes a comment, indicating user approval or agreement.
     *
     * @param commentId Identifier of the comment to be liked.
     * @return ResponseEntity with status 200 OK.
     */
    @PostMapping("/anonymous-posts/comment/like/{commentId}")
    public ResponseEntity<?> like(@PathVariable Integer commentId) {
        commentService.like(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Removes the user's like from a comment.
     *
     * @param commentId Identifier of the comment to be unliked.
     * @return ResponseEntity with status 200 OK.
     */
    @DeleteMapping("/anonymous-posts/comment/like/{commentId}")
    public ResponseEntity<?> unlike(@PathVariable Integer commentId) {
        commentService.unlike(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
