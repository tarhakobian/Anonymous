package com.example.schoolarsanonymouspostingmodule.repository;

import com.example.schoolarsanonymouspostingmodule.model.entity.FrameEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FrameRepository extends CrudRepository<FrameEntity, Long> {
    public Optional<FrameEntity> findFirstByOrderByIdAsc();

    ;
}
