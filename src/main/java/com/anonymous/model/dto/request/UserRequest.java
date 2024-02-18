package com.anonymous.model.dto.request;

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

    @NotNull
    @Size(min = 6)
    private String password;
}