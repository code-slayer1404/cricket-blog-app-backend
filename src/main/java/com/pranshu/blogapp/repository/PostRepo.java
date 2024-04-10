package com.pranshu.blogapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pranshu.blogapp.entity.Post;
import com.pranshu.blogapp.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepo extends JpaRepository<Post, Integer> {

    @Query("SELECT p FROM Post p WHERE p.user = :user ORDER BY p.date DESC")
    public Page<Post> findAllByUser(@Param("user") User user, Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY p.date DESC")
    public Page<Post> findAll(Pageable pageable);

    public List<Post> findAllByUser(User user);

}
