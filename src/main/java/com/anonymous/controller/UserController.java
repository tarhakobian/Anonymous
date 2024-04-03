package com.anonymous.controller;

import com.anonymous.enums.Major;
import com.anonymous.model.dto.User;
import com.anonymous.model.dto.request.LoginRequest;
import com.anonymous.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller class for managing user-related operations.
 * <p>
 * Author: Taron Hakobyan
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Registers a new user.
     *
     * @param request The request body containing user registration details.
     * @return ResponseEntity with the UUID of the registered user and status 201 Created.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User request) {
        UUID uuid = userService.register(request);
        return new ResponseEntity<>(uuid.toString(), HttpStatus.CREATED);
    }

    /**
     * Changes the username of the authenticated user.
     *
     * @param username New username to be set.
     * @return ResponseEntity with the UUID of the user and status 200 OK.
     */
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/users/change-username")
    public ResponseEntity<?> changeUsername(@NotNull @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters long.")
                                            @RequestParam String username) {
        UUID uuid = userService.setUsername(username);
        return ResponseEntity.ok(uuid.toString());
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PatchMapping("/users/set-major")
    public ResponseEntity<?> setMajor(@NotNull @RequestParam Major major){
        userService.setMajor(major);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deactivates the account of the authenticated user.
     *
     * @param confirmedPassword The confirmed password of the user.
     * @return ResponseEntity with status 200 OK.
     */
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/users/deactivate")
    public ResponseEntity<?> deleteAccount(@RequestParam String confirmedPassword) {
        userService.deleteAccount(confirmedPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Reactivates the account with the provided credentials.
     *
     * @param request The request body containing the user's credentials.
     * @return ResponseEntity with status 200 OK.
     */
    @PatchMapping("/users/activate")
    public ResponseEntity<?> reactivateAccount(@RequestBody @Valid LoginRequest request) {
        userService.reactivateAccount(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
