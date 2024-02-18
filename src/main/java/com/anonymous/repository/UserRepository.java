package com.anonymous.repository;

import com.anonymous.model.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Author : Taron Hakobyan
 */
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    /**
     * Finds a user entity by their email address.
     *
     * @param email Email address of the user.
     * @return Optional containing the user entity if found, or empty otherwise.
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Finds a user entity by their username.
     *
     * @param username Username of the user.
     * @return Optional containing the user entity if found, or empty otherwise.
     */
    Optional<UserEntity> findByUsername(String username);
}
