package com.pranshu.blogapp.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pranshu.blogapp.payload.PagedResponse;
import com.pranshu.blogapp.payload.PostDTO;
import com.pranshu.blogapp.service.PostService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostService postService;

    
    @PostMapping("/users/{user_id}/posts")
    public ResponseEntity<PostDTO> addPost(@RequestBody PostDTO postDTO, @PathVariable("user_id") int user_id,@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        PostDTO result = postService.addPost(postDTO, user_id,token);
        // return ResponseEntity.of(Optional.of(result));
        return new ResponseEntity<PostDTO>(result, HttpStatus.CREATED);
    }


    // mapping changed also change on frontend
    @PutMapping("/users/{user_id}/posts/{post_id}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable("post_id") int post_id,
            @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        PostDTO result = postService.updatePost(postDTO, post_id,token);
        return ResponseEntity.of(Optional.of(result));
    }

    // mapping changed also change on frontend

    @DeleteMapping("/users/{user_id}/posts/{post_id}")
    public ResponseEntity<PostDTO> deletePost(@PathVariable("post_id") int post_id,
            @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        PostDTO result = postService.deletePost(post_id,token);
        return ResponseEntity.of(Optional.of(result));
    }
    


    // @GetMapping("/users/{user_id}/posts")
    // public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable("user_id") int user_id) {
    //     List<PostDTO> result = postService.getPostsByUser(user_id);
    //     return ResponseEntity.of(Optional.of(result));
    // }
    @GetMapping("/users/{user_id}/posts")
    public ResponseEntity<PagedResponse<PostDTO>> getPostsByUser(@PathVariable("user_id") int user_id,@RequestParam(defaultValue = "1") int pageNumber) {
        PagedResponse<PostDTO> result = postService.getPostsByUser(user_id,pageNumber);
        return ResponseEntity.of(Optional.of(result));
    }


    

    // @GetMapping("/posts")
    // public ResponseEntity<List<PostDTO>> getAllPosts() {
    //     List<PostDTO> result = postService.getAllPosts();
    //     return ResponseEntity.of(Optional.of(result));
    // }

    // mapping changed also change on frontend
    @GetMapping("/posts")
    public ResponseEntity<PagedResponse<PostDTO>> getAllPosts(@RequestParam(defaultValue = "1") int pageNumber) {
        PagedResponse<PostDTO> result = postService.getAllPosts(pageNumber);
        return ResponseEntity.of(Optional.of(result));
    }

    // mapping changed also change on frontend
    @GetMapping("/posts/{post_id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable("post_id") int post_id) {
        PostDTO result = postService.getPost(post_id);
        return ResponseEntity.of(Optional.of(result));
    }

}
