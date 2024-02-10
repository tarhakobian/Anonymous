package com.example.schoolarsanonymouspostingmodule.repository;

import com.example.schoolarsanonymouspostingmodule.model.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing user entities in the system.
 * <p>
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
