package com.pranshu.blogapp.service;

import java.util.List;

import com.pranshu.blogapp.payload.PostDTO;

public interface PostService {
    PostDTO addPost(PostDTO postDTO,int userId);
    PostDTO updatePost(PostDTO postDTO,int postId);

    
    PostDTO getPost(int postId);
    List<PostDTO> getPostsByUser(int userId);
    List<PostDTO> getAllPosts();


    PostDTO deletePost(int id);
}
