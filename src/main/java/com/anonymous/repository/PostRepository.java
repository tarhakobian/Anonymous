package com.anonymous.repository;

import com.anonymous.model.entity.PostEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * Author : Taron Hakobyan
 */
public interface PostRepository extends JpaRepository<PostEntity, Integer> {

    /**
     * Retrieves all posts with associated entities (publisher, comments, answers, likedBy)
     * ordered by post id in descending order.
     *
     * @return Page of PostEntity containing all posts with associated entities.
     */
    @Cacheable("posts")
    @Query(value = "from PostEntity p left join fetch p.publisher" +
            " left join fetch p.comments c" +
            " left join c.answers" +
            " left join fetch p.likedBy where p.isDeleted = false order by p.createdAt desc")
    Page<PostEntity> findAllOrderedByCreatedAtDesc(Pageable pageable);

    @Query(value = "from PostEntity p left join fetch p.publisher" +
            " left join fetch p.comments c" +
            " left join c.answers" +
            " left join fetch p.likedBy where p.isDeleted = true")
    Set<PostEntity> findByIsDeletedTrue();
}
