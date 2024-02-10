package com.example.schoolarsanonymouspostingmodule.service;

import com.example.schoolarsanonymouspostingmodule.exception.DuplicateUserException;
import com.example.schoolarsanonymouspostingmodule.exception.UserNotFoundException;
import com.example.schoolarsanonymouspostingmodule.model.dto.User;
import com.example.schoolarsanonymouspostingmodule.model.entity.UserEntity;
import com.example.schoolarsanonymouspostingmodule.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing user-related operations in the system.
 * <p>
 * Author : Taron Hakobyan
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves the currently logged-in user.
     *
     * @return UserEntity representing the logged-in user.
     */
    public UserEntity getLoggedInUser() {
        return userRepository.findById(
                        UUID.fromString(String.valueOf(
                                SecurityContextHolder.getContext().getAuthentication().getPrincipal())))
                .orElseThrow(RuntimeException::new);
    }

    /**
     * Loads user details by username.
     *
     * @param username The username (email) of the user.
     * @return UserDetails representing the user details.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                userEntity.getId().toString(),
                userEntity.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole()))
        );
    }

    /**
     * Registers a new user with the provided user details.
     *
     * @param request The user details for registration.
     * @return The unique identifier (UUID) of the registered user.
     * @throws DuplicateUserException If a user with the same email already exists.
     */
    public UUID register(User request) {
        Optional<UserEntity> optional = userRepository.findByEmail(request.getEmail());
        if (optional.isPresent()) throw new DuplicateUserException();

        UserEntity entity = new UserEntity();

        entity.setEmail(request.getEmail());
        entity.setRole("STUDENT");
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setCreatedDate(LocalDateTime.now());

        userRepository.save(entity);

        return entity.getId();
    }

    /**
     * Sets the username for the currently logged-in user.
     *
     * @param username The desired username to be set.
     * @return The unique identifier (UUID) of the user.
     * @throws DuplicateUserException If the specified username is already taken.
     * @throws UserNotFoundException  If the logged-in user is not found.
     */
    public UUID setUsername(String username) {
        Optional<UserEntity> optional = userRepository.findByUsername(username);

        if (optional.isPresent()) throw new DuplicateUserException();

        UUID uuid = UUID.fromString(String.valueOf(SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()));

        UserEntity entity = userRepository.findById(uuid).orElseThrow(UserNotFoundException::new);

        entity.setUsername(username);
        userRepository.save(entity);

        return uuid;
    }

    /**
     * Saves the provided user entity.
     *
     * @param userEntity The user entity to be saved.
     */
    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}
