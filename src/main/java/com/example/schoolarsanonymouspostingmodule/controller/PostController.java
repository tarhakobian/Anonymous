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

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PostController {

    private final PostService postService;

    @GetMapping("/anonymous-posts/")
    public ResponseEntity<?> getAllPosts(@RequestParam(defaultValue = "0") Integer pageNumber,
                                         @RequestParam(defaultValue = "5") Integer size) {
        return ResponseEntity.ok(postService.getAll(pageNumber, size));
    }

    //TODO;: test
    @GetMapping("/anonymous-posts/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable("postId") @NotNull Integer postId) {
        PostResponse response = postService.getById(postId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/anonymous-posts/")
    public ResponseEntity<?> create(@RequestBody @Valid PostRequest request) {
        return ResponseEntity.ok(postService.create(request));
    }

    @PutMapping("/anonymous-posts/{postId}")
    public ResponseEntity<?> edit(@PathVariable("postId") @NotNull Integer postId,
                                  @RequestBody @Valid PostRequest request) {
        postService.edit(postId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/anonymous-posts/{postId}")
    public ResponseEntity<?> delete(@PathVariable("postId") @NotNull Integer postId) {
        postService.delete(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/anonymous-posts/like/{postId}")
    public ResponseEntity<?> like(@PathVariable @NotNull Integer postId) {
        postService.like(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/anonymous-posts/like/{postId}")
    public ResponseEntity<?> unLike(@PathVariable @NotNull Integer postId) {
        postService.unLike(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
