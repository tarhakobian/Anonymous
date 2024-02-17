package com.example.schoolarsanonymouspostingmodule.controller;

import com.example.schoolarsanonymouspostingmodule.model.dto.responce.PostResponse;
import com.example.schoolarsanonymouspostingmodule.service.PostService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        List<PostResponse> postResponses = postService.getAll(pageNumber, size);
        return ResponseEntity.ok(postResponses);
    }

    /**
     * Retrieves a specific anonymous post by its identifier.
     *
     * @param postId Identifier of the post to be retrieved.
     * @return ResponseEntity with a PostResponse object.
     */
    @GetMapping("/anonymous-posts/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable("postId") @Positive Integer postId) {
        PostResponse response = postService.getById(postId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/anonymous-posts/liked-posts")
    public ResponseEntity<?> getLikedPosts() {
        List<PostResponse> postResponses = postService.getLikedPosts();
        return ResponseEntity.ok(postResponses);
    }

    /**
     * Creates a new anonymous post.
     *
     * @param multipartFile  The file to upload for the post.
     * @param usernamePublic Optional flag indicating whether the username should be displayed publicly.
     * @return ResponseEntity with the ID of the created post and status 201 Created.
     */
    @PostMapping(value = "/anonymous-posts/", consumes = {"multipart/form-data"})
    public ResponseEntity<?> create(@RequestParam("file") MultipartFile multipartFile,
                                    @RequestParam(value = "usernamePublic", defaultValue = "false", required = false) boolean usernamePublic) {
        Integer postId = postService.create(multipartFile, usernamePublic);
        return new ResponseEntity<>(postId, HttpStatus.CREATED);
    }

    /**
     * Edits an existing anonymous post.
     *
     * @param postId         Identifier of the post to be edited.
     * @param multipartFile  Optional file to upload for the post.
     * @param usernamePublic Optional flag indicating whether the username should be displayed publicly.
     * @return ResponseEntity with status 200 OK.
     */
    @PutMapping(value = "/anonymous-posts/{postId}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> edit(@PathVariable("postId") @Positive Integer postId,
                                  @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                                  @RequestParam(value = "usernamePublic", defaultValue = "false", required = false) boolean usernamePublic) {
        postService.edit(postId, multipartFile, usernamePublic);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes an existing anonymous post.
     *
     * @param postId Identifier of the post to be deleted.
     * @return ResponseEntity with status 200 OK.
     */
    @DeleteMapping("/anonymous-posts/{postId}")
    public ResponseEntity<?> delete(@PathVariable("postId") @Positive Integer postId) {
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
    public ResponseEntity<?> like(@PathVariable @Positive Integer postId) {
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
    public ResponseEntity<?> unLike(@PathVariable @Positive Integer postId) {
        postService.unLike(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
