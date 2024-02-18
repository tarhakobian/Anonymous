package com.anonymous.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing the request body for user login.
 * <p>
 * Author : Taron Hakobyan
 */
@Getter
@Setter
public class LoginRequest {

    @NotNull
    @Size(max = 50)
    private String email;

    @NotNull
    @Size(max = 50, min = 6)
    private String password;
}
