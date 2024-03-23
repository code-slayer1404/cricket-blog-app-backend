package com.pranshu.blogapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pranshu.blogapp.entity.User;

public interface UserRepo extends JpaRepository<User,Integer> {
    User findByUsername(String username);
}
