package com.example.schoolarsanonymouspostingmodule.repository;

import com.example.schoolarsanonymouspostingmodule.model.entity.CommentEntity;
import com.example.schoolarsanonymouspostingmodule.model.entity.PostEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CommentRepository extends CrudRepository<CommentEntity, Integer> {
    Set<CommentEntity> findByPostAndParentCommentIsNull(PostEntity postEntity);

    Set<CommentEntity> findByParentComment(CommentEntity parentComment);
}
