package com.pranshu.blogapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pranshu.blogapp.entity.Comment;
import com.pranshu.blogapp.entity.Post;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
    public Page<Comment> findAllByPost(Post post,Pageable pageable);
}
