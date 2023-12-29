package com.example.schoolarsanonymouspostingmodule.controller;

import com.example.schoolarsanonymouspostingmodule.service.FrameService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FrameController {

    private final FrameService service;

    @GetMapping("/anonymous-posts/frames/get-current-frame")
    public ResponseEntity<?> getCurrentFrame() {
        return ResponseEntity.ok(service.getCurrentFrameUrl());
    }

    @PostMapping("/anonymous-posts/frames/suggest-frame")
    public ResponseEntity<?> suggestNewFrame(@NotNull @RequestParam MultipartFile frame) {
        service.create(frame);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
