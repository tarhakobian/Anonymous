package com.example.schoolarsanonymouspostingmodule.service;

import com.example.schoolarsanonymouspostingmodule.exception.CommentNotFoundException;
import com.example.schoolarsanonymouspostingmodule.exception.PostNotFoundException;
import com.example.schoolarsanonymouspostingmodule.exception.UserNotFoundException;
import com.example.schoolarsanonymouspostingmodule.model.entity.CommentEntity;
import com.example.schoolarsanonymouspostingmodule.model.entity.PostEntity;
import com.example.schoolarsanonymouspostingmodule.model.entity.UserEntity;
import com.example.schoolarsanonymouspostingmodule.repository.CommentRepository;
import com.example.schoolarsanonymouspostingmodule.repository.PostRepository;
import com.example.schoolarsanonymouspostingmodule.repository.UserRepository;
import com.example.schoolarsanonymouspostingmodule.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public void comment(Integer postId, String content) {
        //find post
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        //find user
        UUID userId = UUID.fromString(String.valueOf(SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()));
        UserEntity publisher = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        //create new comment
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(content);
        commentEntity.setPost(postEntity);
        commentEntity.setPublisher(publisher);

        //save comment
        commentRepository.save(commentEntity);
    }

    public void deleteComment(Integer commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .filter(c -> SecurityUtil.ensureOwnership(c.getPublisher()))
                .orElseThrow(CommentNotFoundException::new);

        commentRepository.delete(commentEntity);
    }

    public void edit(Integer commentId, String content) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .filter(c -> SecurityUtil.ensureOwnership(c.getPublisher()))
                .orElseThrow(CommentNotFoundException::new);

        commentEntity.setContent(content);

        commentRepository.save(commentEntity);
    }

    public Set<CommentEntity> getCommentsForPost(PostEntity postEntity) {
        Set<CommentEntity> topLevelComments = commentRepository.findByPostAndParentCommentIsNull(postEntity);

        for (CommentEntity comment : topLevelComments) {
            fetchReplies(comment);
        }

        return topLevelComments;
    }

    private void fetchReplies(CommentEntity comment) {
        Set<CommentEntity> replies = commentRepository.findByParentComment(comment);
        for (CommentEntity reply : replies) {
            fetchReplies(reply);
        }
        comment.setAnswers(replies);
    }
}
