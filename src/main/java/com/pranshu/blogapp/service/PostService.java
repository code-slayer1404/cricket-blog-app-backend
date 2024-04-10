package com.pranshu.blogapp.service;

import java.util.List;

import com.pranshu.blogapp.payload.PostDTO;

public interface PostService {
    PostDTO addPost(PostDTO postDTO,int userId,String token);
    PostDTO updatePost(PostDTO postDTO,int postId, String token);

    
    PostDTO getPost(int postId);
    List<PostDTO> getPostsByUser(int userId);
    List<PostDTO> getPostsByUser(int userId,int pageNumber);
    List<PostDTO> getAllPosts();
    List<PostDTO> getAllPosts(int pageNumber);


    PostDTO deletePost(int id, String token);
}
