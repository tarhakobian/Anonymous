package com.example.schoolarsanonymouspostingmodule.util;

import com.example.schoolarsanonymouspostingmodule.model.dto.responce.CommentResponse;
import com.example.schoolarsanonymouspostingmodule.model.dto.responce.PostResponse;
import com.example.schoolarsanonymouspostingmodule.model.entity.CommentEntity;
import com.example.schoolarsanonymouspostingmodule.model.entity.PostEntity;
import com.example.schoolarsanonymouspostingmodule.model.entity.UserEntity;

import java.util.stream.Collectors;

/**
 * Utility class for mapping entities to response DTOs.
 * <p>
 * Author : Taron Hakobyan
 */
public class Mapper {

    /**
     * Maps a PostEntity to a PostResponse DTO.
     *
     * @param entity The PostEntity to be mapped.
     * @return A PostResponse DTO representing the mapped information.
     */
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

        // Include the username in the response if usernamePublic is true
        if (entity.getUsernamePublic()) {
            response.setUsername(entity.getPublisher().getUsername());
        }

        return response;
    }

    /**
     * Maps a CommentEntity to a CommentResponse DTO.
     *
     * @param entity The CommentEntity to be mapped.
     * @return A CommentResponse DTO representing the mapped information.
     */

    public static CommentResponse mapComment(CommentEntity entity) {
        CommentResponse response = new CommentResponse();
        response.setId(entity.getId());
        //TODO ; change to username
        response.setUsername(entity.getPublisher().getUsername());
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

}
