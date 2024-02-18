package com.anonymous.service;

import com.anonymous.exception.CommentDoesNotBelongToThePostException;
import com.anonymous.exception.CommentNotFoundException;
import com.anonymous.exception.PostNotFoundException;
import com.anonymous.model.dto.request.CommentRequest;
import com.anonymous.model.entity.CommentEntity;
import com.anonymous.model.entity.PostEntity;
import com.anonymous.model.entity.UserEntity;
import com.anonymous.repository.CommentRepository;
import com.anonymous.repository.PostRepository;
import com.anonymous.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing comments in the system.
 * <p>
 * Author: Taron Hakobyan
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    /**
     * Creates a new comment for a specific post.
     *
     * @param request The request containing the post ID, parent comment ID (if any), and comment content.
     * @return The identifier of the newly created comment.
     */
    public Integer comment(CommentRequest request) {
        PostEntity postEntity = postRepository.findById(request.getPostId())
                .orElseThrow(PostNotFoundException::new);

        CommentEntity parent = null;

        if (request.getParentCommentId() != null) {
            parent = commentRepository.findById(request.getParentCommentId()).
                    orElseThrow(CommentNotFoundException::new);

            if (!Objects.equals(parent.getPost().getId(), request.getPostId())) {
                throw new CommentDoesNotBelongToThePostException();
            }
        }

        UserEntity publisher = userService.getLoggedInUser();

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(request.getContent());
        commentEntity.setPost(postEntity);
        commentEntity.setPublisher(publisher);
        commentEntity.setParentComment(parent);
        commentEntity.setUsernamePublic(request.getUsernamePublic());

        commentRepository.save(commentEntity);

        return commentEntity.getId();
    }

    /**
     * Deletes a comment based on its identifier.
     *
     * @param commentId The identifier of the comment to be deleted.
     */
    public void deleteComment(Integer commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .filter(c -> SecurityUtil.ensureOwnership(c.getPublisher()))
                .orElseThrow(CommentNotFoundException::new);

        commentRepository.delete(commentEntity);
    }

    /**
     * Edits the content of a comment based on its identifier.
     *
     * @param commentId The identifier of the comment to be edited.
     * @param content   The updated content for the comment.
     */
    public void edit(Integer commentId, String content) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .filter(c -> SecurityUtil.ensureOwnership(c.getPublisher()))
                .orElseThrow(CommentNotFoundException::new);

        commentEntity.setContent(content);

        commentRepository.save(commentEntity);
    }

    /**
     * Retrieves all comments for a specific post along with their associated replies.
     * This method returns a Set of top-level comments for the given post entity,
     * and for each top-level comment, it recursively fetches and sets its associated replies.
     * The 'answers' property of each top-level comment is populated with its child comments.
     *
     * @param postEntity The post for which comments are to be retrieved.
     * @return Set of top-level comments with their associated replies.
     */
    public Set<CommentEntity> getCommentsForPost(PostEntity postEntity) {
        // Retrieve top-level comments for the specified post
        Set<CommentEntity> topLevelComments = commentRepository.findByPostAndParentCommentIsNull(postEntity);

        // For each top-level comment, recursively fetch and set associated replies
        return topLevelComments.stream()
                .peek(this::fetchReplies)
                .collect(Collectors.toSet());
    }


    /**
     * Recursively fetches and sets replies for a specific comment.
     * The method populates the 'answers' property of the given comment
     * with its associated child comments.
     *
     * @param comment The comment for which replies are to be fetched.
     */
    private void fetchReplies(CommentEntity comment) {
        // Retrieve child comments for the current comment
        Set<CommentEntity> replies = commentRepository.findByParentComment(comment);

        // Recursively fetch replies for each child comment
        for (CommentEntity reply : replies) {
            fetchReplies(reply);
        }

        // Set the 'answers' property of the comment with its associated replies
        comment.setAnswers(replies);
    }


    /**
     * Likes a specific comment.
     *
     * @param commentId The identifier of the comment to be liked.
     */
    public void like(Integer commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        UserEntity userEntity = userService.getLoggedInUser();

        userEntity.getLikedComments().add(commentEntity);
        commentEntity.getLikedBy().add(userEntity);

        userService.save(userEntity);
        commentRepository.save(commentEntity);
    }

    /**
     * Removes the like from a specific comment.
     *
     * @param commentId The identifier of the comment from which the like is to be removed.
     */
    public void unlike(Integer commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        UserEntity userEntity = userService.getLoggedInUser();

        userEntity.getLikedComments().remove(commentEntity);
        commentEntity.getLikedBy().remove(userEntity);

        userService.save(userEntity);
        commentRepository.save(commentEntity);
    }
}
