package com.example.schoolarsanonymouspostingmodule.model.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO representing the response for a comment in the system.
 * <p>
 * Author : Taron Hakobyan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    /**
     * Unique identifier of the comment.
     */
    private Integer id;

    /**
     * Username of the publisher.
     */
    private String username;

    /**
     * Content of the comment.
     */
    private String content;

    /**
     * Identifier of the parent comment (if any).
     */
    private Integer parentCommentId;

    /**
     * Set of comment responses representing answers to this comment.
     */
    private Set<CommentResponse> answers;
}
