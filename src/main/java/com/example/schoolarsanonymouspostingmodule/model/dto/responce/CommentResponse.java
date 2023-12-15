package com.example.schoolarsanonymouspostingmodule.model.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Integer id;
    private UUID userId;
    private String content;
    private Integer parentCommentId;
    private Set<CommentResponse> answers;
}
