package com.example.schoolarsanonymouspostingmodule.repository;

import com.example.schoolarsanonymouspostingmodule.model.entity.PostEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<PostEntity, Integer> {

    @Query(value = "from PostEntity p left join fetch p.publisher" +
            " left join fetch p.comments c" +
            " left join c.answers" +
            " left join fetch p.likedBy ")
    List<PostEntity> findAllOrderById();

}
