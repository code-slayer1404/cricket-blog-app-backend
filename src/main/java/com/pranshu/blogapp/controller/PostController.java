package com.pranshu.blogapp.controller;


import java.util.Optional;

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

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Note: Although the incoming PostDTO could technically include a User field,
     * it will be ignored during deserialization because the Post entity
     * uses @JsonIgnore
     * on its 'user' field instead of @JsonBackReference (used together with @JsonManagedReference).
     *
     * This is intentional — we do not rely on client-supplied user data for
     * security reasons.
     * Instead, we always fetch the authenticated user from the SecurityContext and
     * manually
     * set it on the Post entity before saving. This ensures ownership is
     * server-controlled
     * and avoids any risk of spoofed or incorrect user references.
     */
    @PostMapping("/users/{user_id}/posts")
    public ResponseEntity<PostDTO> addPost(@RequestBody PostDTO postDTO) {

        PostDTO result = postService.addPost(postDTO);
        // return ResponseEntity.of(Optional.of(result));
        return new ResponseEntity<PostDTO>(result, HttpStatus.CREATED);
    }


    // mapping changed also change on frontend
    @PutMapping("/users/{user_id}/posts/{post_id}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable("post_id") int post_id) {

        PostDTO result = postService.updatePost(postDTO, post_id);
        return ResponseEntity.of(Optional.of(result));
    }

    // mapping changed also change on frontend

    @DeleteMapping("/users/{user_id}/posts/{post_id}")
    public ResponseEntity<PostDTO> deletePost(@PathVariable("post_id") int post_id) {
        PostDTO result = postService.deletePost(post_id);
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
