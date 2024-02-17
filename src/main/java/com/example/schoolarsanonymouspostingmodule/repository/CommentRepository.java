package com.example.schoolarsanonymouspostingmodule.repository;

import com.example.schoolarsanonymouspostingmodule.model.entity.CommentEntity;
import com.example.schoolarsanonymouspostingmodule.model.entity.PostEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Repository interface for managing comments in the system.
 * <p>
 * Author : Taron Hakobyan
 */
public interface CommentRepository extends CrudRepository<CommentEntity, Integer> {

    /**
     * Retrieves top-level comments for a specific post.
     *
     * @param postEntity The post for which top-level comments are to be retrieved.
     * @return Set of top-level comments associated with the specified post.
     */
    @Cacheable("top_level_comments")
    Set<CommentEntity> findByPostAndParentCommentIsNull(PostEntity postEntity);

    /**
     * Retrieves child comments for a specific parent comment.
     *
     * @param parentComment The parent comment for which child comments are to be retrieved.
     * @return Set of child comments associated with the specified parent comment.
     */
    @Cacheable("sub_comments")
    Set<CommentEntity> findByParentComment(CommentEntity parentComment);
}
