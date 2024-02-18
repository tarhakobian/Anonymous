package com.anonymous.service;

import com.anonymous.model.dto.User;
import com.anonymous.exception.DuplicateUserException;
import com.anonymous.exception.IncorrectPasswordException;
import com.anonymous.exception.UserNotFoundException;
import com.anonymous.model.dto.request.LoginRequest;
import com.anonymous.model.entity.UserEntity;
import com.anonymous.repository.PostRepository;
import com.anonymous.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing user-related operations in the system.
 * Author: Taron Hakobyan
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;

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
                true,
                true,
                true,
                userEntity.getActive(),
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
        entity.setRole("ROLE_STUDENT");
        entity.setPassword(passwordEncoder.encode(request.getPassword()));

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

    /**
     * Deletes the account of the currently logged-in user.
     *
     * @param confirmedPassword The password confirmation to delete the account.
     */
    @Transactional
    public void deleteAccount(String confirmedPassword) {
        UserEntity entity = userRepository.findById(
                        UUID.fromString(String.valueOf(SecurityContextHolder.getContext()
                                .getAuthentication().getPrincipal())))
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(confirmedPassword, entity.getPassword())) {
            throw new IncorrectPasswordException();
        }

        postRepository.saveAll(
                entity.getPosts().stream().peek(postEntity -> postEntity.setIsDeleted(true))
                        .collect(Collectors.toSet())
        );

        entity.setActive(false);
    }

    /**
     * Reactivates a user account with the provided login request details.
     *
     * @param request The login request containing the email and password for reactivation.
     */
    public void reactivateAccount(LoginRequest request) {
        UserEntity userEntity = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            userEntity.setActive(true);
        } else {
            throw new IncorrectPasswordException();
        }
    }
}
