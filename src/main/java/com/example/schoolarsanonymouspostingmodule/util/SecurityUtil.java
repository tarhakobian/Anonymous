package com.example.schoolarsanonymouspostingmodule.util;

import com.example.schoolarsanonymouspostingmodule.exception.IllegalActionException;
import com.example.schoolarsanonymouspostingmodule.exception.UserNotFoundException;
import com.example.schoolarsanonymouspostingmodule.model.entity.UserEntity;
import com.example.schoolarsanonymouspostingmodule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Utility class for security-related operations.
 * <p>
 * Author : Taron Hakobyan
 */
@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private static UserRepository userRepository;

    @Autowired
    public SecurityUtil(UserRepository userRepository) {
        SecurityUtil.userRepository = userRepository;
    }

    /**
     * Ensures that the currently logged-in user is the owner of a specified entity.
     *
     * @param owner The owner entity to be checked.
     * @return True if the logged-in user is the owner, otherwise throws IllegalActionException.
     * @throws IllegalActionException If the logged-in user is not the owner.
     */
    public static boolean ensureOwnership(UserEntity owner) {
        // Get the UUID of the currently logged-in user
        UUID userId = UUID.fromString(
                String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));


        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // Check if the logged-in user is the owner
        if (userId.equals(owner.getId()) || userEntity.getRole().equals("ADMIN")) {
            return true;
        }

        throw new IllegalActionException("You are not authorized to perform this action.");
    }
}



