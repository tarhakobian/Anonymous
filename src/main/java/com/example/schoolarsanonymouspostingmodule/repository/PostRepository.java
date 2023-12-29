package com.example.schoolarsanonymouspostingmodule.repository;

import com.example.schoolarsanonymouspostingmodule.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {

    @Query(value = "from PostEntity p left join fetch p.publisher" +
            " left join fetch p.comments c" +
            " left join c.answers" +
            " left join fetch p.likedBy order by p.id ")
    Page<PostEntity> findAllOrderByIdDesc(Pageable pageable);
}
