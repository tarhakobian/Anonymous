package com.example.schoolarsanonymouspostingmodule.controller;

import com.example.schoolarsanonymouspostingmodule.model.dto.request.PostRequest;
import com.example.schoolarsanonymouspostingmodule.model.dto.responce.PostResponse;
import com.example.schoolarsanonymouspostingmodule.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing anonymous posts.
 * <p>
 * Author: Taron Hakobyan
 */
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PostController {

    private final PostService postService;

    /**
     * Retrieves all anonymous posts with pagination.
     *
     * @param pageNumber Page number (default: 0).
     * @param size       Number of posts per page (default: 5).
     * @return ResponseEntity with a list of PostResponse objects.
     */
    @GetMapping("/anonymous-posts/")
    public ResponseEntity<?> getAllPosts(@RequestParam(defaultValue = "0") Integer pageNumber,
                                         @RequestParam(defaultValue = "5") Integer size) {
        return ResponseEntity.ok(postService.getAll(pageNumber, size));
    }

    /**
     * Retrieves a specific anonymous post by its identifier.
     *
     * @param postId Identifier of the post to be retrieved.
     * @return ResponseEntity with a PostResponse object.
     */
    @GetMapping("/anonymous-posts/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable("postId") @NotNull Integer postId) {
        PostResponse response = postService.getById(postId);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new anonymous post.
     *
     * @param request The request body containing the post details.
     * @return ResponseEntity with the created PostResponse object.
     */
    @PostMapping("/anonymous-posts/")
    public ResponseEntity<?> create(@RequestBody @Valid PostRequest request) {
        return ResponseEntity.ok(postService.create(request));
    }

    /**
     * Edits an existing anonymous post.
     *
     * @param postId  Identifier of the post to be edited.
     * @param request The request body containing the updated post details.
     * @return ResponseEntity with status 200 OK.
     */
    @PutMapping("/anonymous-posts/{postId}")
    public ResponseEntity<?> edit(@PathVariable("postId") @NotNull Integer postId,
                                  @RequestBody @Valid PostRequest request) {
        postService.edit(postId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes an existing anonymous post.
     *
     * @param postId Identifier of the post to be deleted.
     * @return ResponseEntity with status 200 OK.
     */
    @DeleteMapping("/anonymous-posts/{postId}")
    public ResponseEntity<?> delete(@PathVariable("postId") @NotNull Integer postId) {
        postService.delete(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Likes an anonymous post, indicating user approval or agreement.
     *
     * @param postId Identifier of the post to be liked.
     * @return ResponseEntity with status 200 OK.
     */
    @PostMapping("/anonymous-posts/like/{postId}")
    public ResponseEntity<?> like(@PathVariable @NotNull Integer postId) {
        postService.like(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Removes the user's like from an anonymous post.
     *
     * @param postId Identifier of the post to be unliked.
     * @return ResponseEntity with status 200 OK.
     */
    @DeleteMapping("/anonymous-posts/like/{postId}")
    public ResponseEntity<?> unLike(@PathVariable @NotNull Integer postId) {
        postService.unLike(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
