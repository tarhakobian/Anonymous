package com.example.schoolarsanonymouspostingmodule.service;

import com.example.schoolarsanonymouspostingmodule.exception.DuplicateUserException;
import com.example.schoolarsanonymouspostingmodule.exception.UserNotFoundException;
import com.example.schoolarsanonymouspostingmodule.model.dto.User;
import com.example.schoolarsanonymouspostingmodule.model.entity.UserEntity;
import com.example.schoolarsanonymouspostingmodule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity getLoggedInUser() {
        return userRepository.findById(
                        UUID.fromString(String.valueOf(
                                SecurityContextHolder.getContext().getAuthentication().getPrincipal())))
                .orElseThrow(RuntimeException::new);
    }

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

    public UUID register(User request) {
        Optional<UserEntity> optional = userRepository.findByEmail(request.getEmail());
        if (optional.isPresent()) throw new DuplicateUserException();

        UserEntity entity = new UserEntity();

        entity.setEmail(request.getEmail());
        entity.setRole("STUDENT");
        entity.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(entity);

        return entity.getId();
    }

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

    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}
