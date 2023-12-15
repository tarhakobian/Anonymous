package com.example.schoolarsanonymouspostingmodule.controller;

import com.example.schoolarsanonymouspostingmodule.model.dto.User;
import com.example.schoolarsanonymouspostingmodule.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User request) {
        UUID uuid = service.register(request);
        return new ResponseEntity<>(uuid, HttpStatus.CREATED);
    }

    @PatchMapping("users/change-username")
    public ResponseEntity<?> changeUsername(@NotNull @RequestParam String username) {
        UUID uuid = service.setUsername(username);
        return ResponseEntity.ok(uuid);
    }
}
