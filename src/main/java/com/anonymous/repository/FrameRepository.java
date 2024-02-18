package com.anonymous.repository;

import com.anonymous.model.entity.FrameEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Author : Taron Hakobyan
 */
public interface FrameRepository extends JpaRepository<FrameEntity, Integer> {
    @Cacheable("frames")
    Page<FrameEntity> findAll(Pageable pageable);
}
