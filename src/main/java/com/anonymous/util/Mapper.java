package com.anonymous.util;

import com.anonymous.model.dto.responce.CommentResponse;
import com.anonymous.model.dto.responce.FrameResponse;
import com.anonymous.model.dto.responce.PostResponse;
import com.anonymous.model.entity.CommentEntity;
import com.anonymous.model.entity.FrameEntity;
import com.anonymous.model.entity.PostEntity;
import com.anonymous.model.entity.UserEntity;

import java.util.stream.Collectors;

/**
 * Utility class for mapping entities to response DTOs.
 * <p>
 * Author : Taron Hakobyan
 */
public class Mapper {
    public static PostResponse mapPost(PostEntity entity) {
        PostResponse response = PostResponse.builder()
                .id(entity.getId())
                .url(entity.getUrl())
                .likedBy(entity.getLikedBy().stream()
                        .map(UserEntity::getUsername)
                        .collect(Collectors.toSet()))
                .comments(entity.getComments().stream()
                        .map(Mapper::mapComment)
                        .collect(Collectors.toSet()))
                .build();

        if (entity.getUsernamePublic()) {
            response.setUsername(entity.getPublisher().getUsername());
        } else {
            response.setUsername("Anonymous");
        }

        return response;
    }

    public static CommentResponse mapComment(CommentEntity entity) {
        CommentResponse response = new CommentResponse();
        response.setId(entity.getId());
        if (entity.getUsernamePublic()) {
            response.setUsername(entity.getPublisher().getUsername());
        } else response.setUsername("Anonymous");
        response.setContent(entity.getContent());

        // Include parent comment id if the comment has a parent
        if (entity.getParentComment() != null) {
            response.setParentCommentId(entity.getParentComment().getId());
        }

        // Map and include answers (nested comments)
        if (!entity.getAnswers().isEmpty()) {
            response.setAnswers(entity.getAnswers()
                    .stream()
                    .map(Mapper::mapComment) // Recursively map answers
                    .collect(Collectors.toSet()));
        }

        return response;
    }

    public static FrameResponse mapFrame(FrameEntity frameEntity) {
        FrameResponse frameResponse = new FrameResponse();

        frameResponse.setFrameId(frameResponse.getFrameId());
        frameResponse.setUrl(frameEntity.getUrl());

        return frameResponse;
    }
}
