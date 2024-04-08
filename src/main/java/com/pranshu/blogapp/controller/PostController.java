package com.pranshu.blogapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostService postService;

    /**
     * Add a new post for a specific user.
     *
     * @param postDTO the post data transfer object
     * @param user_id the ID of the user
     * @return the response entity containing the added post data transfer object
     */
    @PostMapping("/users/{user_id}/posts")
    public ResponseEntity<PostDTO> addPost(@RequestBody PostDTO postDTO, @PathVariable("user_id") int user_id,@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        PostDTO result = postService.addPost(postDTO, user_id,token);
        // return ResponseEntity.of(Optional.of(result));
        return new ResponseEntity<PostDTO>(result, HttpStatus.CREATED);
    }

    /**
     * Updates a post with the given postDTO and post_id.
     *
     * @param postDTO the post data to update
     * @param post_id the id of the post to update
     * @return the updated PostDTO wrapped in a ResponseEntity
     */
    @PutMapping("/posts/{post_id}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable("post_id") int post_id,
            @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        PostDTO result = postService.updatePost(postDTO, post_id,token);
        return ResponseEntity.of(Optional.of(result));
    }

    /**
     * Deletes a post by its ID.
     *
     * @param post_id the ID of the post to be deleted
     * @return the deleted post as a ResponseEntity
     */
    @DeleteMapping("/posts/{post_id}")
    public ResponseEntity<PostDTO> deletePost(@PathVariable("post_id") int post_id,
            @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        PostDTO result = postService.deletePost(post_id,token);
        return ResponseEntity.of(Optional.of(result));
    }
    

    /**
     * Retrieves a list of post data transfer objects by user ID.
     *
     * @param  user_id	The ID of the user to retrieve posts for
     * @return         	An optional containing the list of post data transfer objects
     */
    @GetMapping("/users/{user_id}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable("user_id") int user_id) {
        List<PostDTO> result = postService.getPostsByUser(user_id);
        return ResponseEntity.of(Optional.of(result));
    }


    /**
     * Retrieves all posts.
     *
     * @return         	List of PostDTO objects
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> result = postService.getAllPosts();
        return ResponseEntity.of(Optional.of(result));
    }

    /**
     * Retrieves a post by its ID.
     *
     * @param  post_id	the ID of the post to retrieve
     * @return         	the post DTO wrapped in a ResponseEntity
     */
    @GetMapping("/posts/{post_id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable("post_id") int post_id) {
        PostDTO result = postService.getPost(post_id);
        return ResponseEntity.of(Optional.of(result));
    }

}
