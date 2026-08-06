package com.pranshu.blogapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pranshu.blogapp.entity.Post;
import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.payload.PostDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepo extends JpaRepository<Post, Integer> {

    @Query("SELECT p FROM Post p WHERE p.user = :user ORDER BY p.date DESC")
    public Page<Post> findAllByUser(@Param("user") User user, Pageable pageable);

    // @Query("SELECT p FROM Post p ORDER BY p.date DESC")
    // public Page<Post> findAll(Pageable pageable);

    @Query("SELECT new com.pranshu.blogapp.payload.PostDTO(p.id,p.title,p.content,p.date,new com.pranshu.blogapp.payload.UserDTO(u.id,u.name,u.username)) "+"FROM Post p JOIN p.user u ORDER BY p.date DESC")
    public Page<PostDTO> findAllTest(Pageable pageable);

    public List<Post> findAllByUser(User user);

}
