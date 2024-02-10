package com.example.schoolarsanonymouspostingmodule.repository;

import com.example.schoolarsanonymouspostingmodule.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing posts in the system.
 * <p>
 * Author : Taron Hakobyan
 */
public interface PostRepository extends JpaRepository<PostEntity, Integer> {

    /**
     * Retrieves all posts with associated entities (publisher, comments, answers, likedBy)
     * ordered by post id in descending order.
     *
     * @param pageable Pageable object for pagination.
     * @return Page of PostEntity containing all posts with associated entities.
     */
    @Query(value = "from PostEntity p left join fetch p.publisher" +
            " left join fetch p.comments c" +
            " left join c.answers" +
            " left join fetch p.likedBy order by p.id ")
    Page<PostEntity> findAllOrderByIdDesc(Pageable pageable);
}
