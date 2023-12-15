package com.example.schoolarsanonymouspostingmodule.model.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private String url;
    private String username;
    private Set<UUID> likedBy;
    private Set<CommentResponse> comments;
}
