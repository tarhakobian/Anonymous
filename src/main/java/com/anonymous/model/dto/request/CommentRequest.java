package com.anonymous.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing the request body for creating a new comment.
 * <p>
 * Author : Taron Hakobyan
 */
@Getter
@Setter
public class CommentRequest {

    @NotNull
    @Positive
    private Integer postId;

    @Positive
    private Integer parentCommentId;

    @NotNull
    @Size(max = 75)
    private String content;

    private Boolean usernamePublic = false;
}