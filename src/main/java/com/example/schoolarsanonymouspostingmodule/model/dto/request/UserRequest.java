package com.example.schoolarsanonymouspostingmodule.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * DTO representing a user in the system.
 * <p>
 * Author : Taron Hakobyan
 */
@Getter
@Setter
public class UserRequest {

    private UUID id;

    /**
     * Email address of the user.
     * Should match the pattern ".*@student\.glendale\.edu".
     */

    @NotNull
    @Email(regexp = ".*@student\\.glendale\\.edu")
    private String email;

    /**
     * Password of the user.
     * Should have a minimum length of 6 characters.
     * Access is set to WRITE_ONLY to ensure it is not exposed in responses.
     */
    @NotNull
    @Size(min = 6)
    private String password;
}