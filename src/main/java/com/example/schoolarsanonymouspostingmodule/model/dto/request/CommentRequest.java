package com.example.schoolarsanonymouspostingmodule.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    @NotNull
    private Integer postId;
    @NotNull
    private String content;
}
