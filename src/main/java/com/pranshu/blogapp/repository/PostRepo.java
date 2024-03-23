package com.pranshu.blogapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pranshu.blogapp.entity.Post;
import com.pranshu.blogapp.entity.User;

public interface PostRepo extends JpaRepository<Post,Integer> {
    public List<Post> findAllByUser(User user);
}
