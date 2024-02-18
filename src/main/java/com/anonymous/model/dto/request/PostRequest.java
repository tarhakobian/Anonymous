package com.anonymous.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO representing the request body for creating a new post.
 * <p>
 * Author : Taron Hakobyan
 */
@Getter
@Setter
public class PostRequest {
    @NotNull
    private MultipartFile file;

    /**
     * Flag indicating whether the username should be made public.
     */
    private boolean usernamePublic = false;
}
