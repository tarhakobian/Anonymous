package com.example.schoolarsanonymouspostingmodule.model.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * DTO representing the response for a post in the system.
 * Author: Taron Hakobyan
 */
@Data
@AllArgsConstructor
@Builder
public class PostResponse {
    /**
     * Unique identifier of the post.
     */
    private Integer id;

    /**
     * URL of the post referring to S3.
     */
    private String url;

    /**
     * Username associated with the post.
     */
    private String username;

    /**
     * Set of unique identifiers of users who liked the post.
     */
    private Set<String> likedBy;

    /**
     * Set of comments in DTO form associated with the post.
     */
    private Set<CommentResponse> comments;
}
