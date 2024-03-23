package com.pranshu.blogapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pranshu.blogapp.payload.PostDTO;
import com.pranshu.blogapp.service.PostService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/users/{user_id}/posts")
    public ResponseEntity<PostDTO> addPost(@RequestBody PostDTO postDTO,@PathVariable("user_id") int user_id){
        PostDTO result = postService.addPost(postDTO,user_id);
        return ResponseEntity.of(Optional.of(result));
    }

    @PutMapping("/posts/{post_id}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO,@PathVariable("post_id") int post_id) {
        PostDTO result = postService.updatePost(postDTO,post_id);
        return ResponseEntity.of(Optional.of(result));
    }
    @DeleteMapping("/posts/{post_id}")
    public ResponseEntity<PostDTO> deletePost(@RequestBody PostDTO postDTO,@PathVariable("post_id") int post_id) {
        PostDTO result = postService.deletePost(post_id);
        return ResponseEntity.of(Optional.of(result));
    }







    @GetMapping("/users/{user_id}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable("user_id") int user_id){
        List<PostDTO> result = postService.getPostsByUser(user_id);
        return ResponseEntity.of(Optional.of(result));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getAllPosts(){
        List<PostDTO> result = postService.getAllPosts();
        return ResponseEntity.of(Optional.of(result));
    }

    @GetMapping("/posts/{post_id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable("post_id") int post_id){
        PostDTO result = postService.getPost(post_id);
        return ResponseEntity.of(Optional.of(result));
    }
    
}
