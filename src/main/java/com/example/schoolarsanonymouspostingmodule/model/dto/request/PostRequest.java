package com.example.schoolarsanonymouspostingmodule.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostRequest {
    @NotNull
    private MultipartFile file;
    private boolean usernamePublic = false;
}
