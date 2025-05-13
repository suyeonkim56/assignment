package com.example.assignment.auth.repository;

import com.example.assignment.auth.entity.User;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public interface AuthRepository {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    User save(User user);
    Optional<User> findById(Long id);

}

