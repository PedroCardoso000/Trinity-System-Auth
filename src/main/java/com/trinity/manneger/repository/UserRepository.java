package com.trinity.manneger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trinity.manneger.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
