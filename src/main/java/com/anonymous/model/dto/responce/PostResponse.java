package com.anonymous.model.dto.responce;

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

    private Integer id;

    private String url;

    private String username;

    private Set<String> likedBy;

    private Set<CommentResponse> comments;
}
