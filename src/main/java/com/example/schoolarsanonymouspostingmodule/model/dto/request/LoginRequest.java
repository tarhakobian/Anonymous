package com.example.schoolarsanonymouspostingmodule.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing the request body for user login.
 * <p>
 * Author : Taron Hakobyan
 * <p>
 * Note : Next update will add username as an alternative for login
 */
@Getter
@Setter
public class LoginRequest {

    /**
     * User's email for login.
     */
    @NotNull
    private String email;

    /**
     * User's password for login.
     */
    @NotNull
    private String password;
}
