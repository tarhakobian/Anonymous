package com.example.schoolarsanonymouspostingmodule.util;

import com.example.schoolarsanonymouspostingmodule.model.dto.responce.CommentResponse;
import com.example.schoolarsanonymouspostingmodule.model.dto.responce.PostResponse;
import com.example.schoolarsanonymouspostingmodule.model.entity.CommentEntity;
import com.example.schoolarsanonymouspostingmodule.model.entity.PostEntity;
import com.example.schoolarsanonymouspostingmodule.model.entity.UserEntity;

import java.util.stream.Collectors;

public class Mapper {
    public static PostResponse mapPost(PostEntity entity) {
        PostResponse response = PostResponse.builder()
                .id(entity.getId())
                .url(entity.getUrl())
                .likedBy(entity.getLikedBy().stream()
                        .map(UserEntity::getId)
                        .collect(Collectors.toSet()))
                .comments(entity.getComments().stream()
                        .map(Mapper::mapComment)
                        .collect(Collectors.toSet()))
                .build();

        if (entity.getUsernamePublic()) {
            response.setUsername(entity.getPublisher().getUsername());
        }

        return response;
    }

    public static CommentResponse mapComment(CommentEntity entity) {

        CommentResponse response = new CommentResponse();
        response.setId(entity.getId());
        response.setUserId(entity.getPublisher().getId());
        response.setContent(entity.getContent());

        if (entity.getParentComment() != null) {
            response.setParentCommentId(entity.getParentComment().getId());
        }

        if (!entity.getAnswers().isEmpty()) {
            response.setAnswers(entity.getAnswers()
                    .stream()
                    .map(Mapper::mapComment) // Recursively map answers
                    .collect(Collectors.toSet()));
        }

        return response;
    }

}
