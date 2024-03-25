package com.pranshu.blogapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pranshu.blogapp.entity.User;

public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
}
