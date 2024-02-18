package com.anonymous.model.dto.responce;

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
    private Integer id;

    private String username;

    private String content;

    private Integer parentCommentId;

    private Set<CommentResponse> answers;
}
