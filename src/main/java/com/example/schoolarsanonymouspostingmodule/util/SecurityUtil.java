package com.example.schoolarsanonymouspostingmodule.util;

import com.example.schoolarsanonymouspostingmodule.exception.IllegalActionException;
import com.example.schoolarsanonymouspostingmodule.model.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtil {

    public static boolean ensureOwnership(UserEntity owner) {
        UUID userId = UUID.fromString(
                String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));

        if (!userId.equals(owner.getId())) {
            throw new IllegalActionException();
        }

        return true;
    }
}
