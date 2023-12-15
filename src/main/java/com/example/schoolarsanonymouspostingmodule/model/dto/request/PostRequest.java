package com.example.schoolarsanonymouspostingmodule.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostRequest {
    //TODO: add @NotNull after integrating with S3
    private MultipartFile file = null;
    private boolean usernamePublic = false;
}
