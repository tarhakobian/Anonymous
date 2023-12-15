package com.example.schoolarsanonymouspostingmodule.controller;

import com.example.schoolarsanonymouspostingmodule.model.dto.request.PostRequest;
import com.example.schoolarsanonymouspostingmodule.model.dto.responce.PostResponse;
import com.example.schoolarsanonymouspostingmodule.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/anonymous-posts/")
    public ResponseEntity<?> getAllPosts() {
        return ResponseEntity.ok(postService.getAll());
    }

    //TODO;: test
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/anonymous-posts/{post-id}")
    public ResponseEntity<?> getPostById(@PathVariable("post-id") Integer postId) {
        PostResponse response = postService.getById(postId);
        return ResponseEntity.ok(response);
    }

    //TODO : add @Valid after integrating with S3
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/anonymous-posts/")
    public ResponseEntity<?> create(@RequestBody PostRequest request) {

        return ResponseEntity.ok(postService.create(request));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/anonymous-posts/{post-id}")
    public ResponseEntity<?> edit(@PathVariable("post-id") Integer postId,
                                  @RequestBody PostRequest request) {
        postService.edit(postId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/anonymous-posts/{post-id}")
    public ResponseEntity<?> delete(@PathVariable("post-id") Integer postId) {
        postService.delete(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/anonymous-posts/like/{postId}")
    public ResponseEntity<?> like(@PathVariable Integer postId) {
        postService.like(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/anonymous-posts/like/{postId}")
    public ResponseEntity<?> unLike(@PathVariable Integer postId) {
        postService.unLike(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
