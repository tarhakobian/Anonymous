package com.example.schoolarsanonymouspostingmodule.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    /**
     * Identifier of the post on which the comment is being made.
     */
    @NotNull
    @Positive
    private Integer postId;

    @Positive
    Integer parentCommentId;
    /**
     * The content of the comment.
     */
    @NotNull
    private String content;
}