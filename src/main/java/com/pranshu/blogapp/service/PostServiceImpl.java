package com.pranshu.blogapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pranshu.blogapp.entity.Post;
import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.exception.CustomException;
import com.pranshu.blogapp.payload.PostDTO;
import com.pranshu.blogapp.repository.PostRepo;
import com.pranshu.blogapp.repository.UserRepo;
import com.pranshu.blogapp.security.UserValidator;

@Service
@SuppressWarnings(value = {"unused"})
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserValidator userValidator;
    
    @Override
    public PostDTO addPost(PostDTO postDTO,int userId,String token) {

        // to ensure the current user is the one making the request
        User currentUser = userValidator.validateUser(userId, token);

        User user = userRepo.findById(userId).orElseThrow(()->{
            throw new CustomException("User not found with id: "+userId);
        });

        Post post = modelMapper.map(postDTO, Post.class);

        post.setUser(user);
        user.getPosts().add(post);
        Post savedPost = postRepo.save(post);
        userRepo.save(user);
        
        return modelMapper.map(savedPost, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO,int postId, String token) {

        Post requestedPost = userValidator.validatePost(postId, token);

        Post post = postRepo.findById(postId).orElseThrow(() -> {
            throw new CustomException("Post not found with id: " + postId);
        });

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setDate(postDTO.getDate());
        
        Post updatedPost = postRepo.save(post);
        return modelMapper.map(updatedPost, PostDTO.class);
    }



    @Override
    public PostDTO deletePost(int id, String token) {

        Post requestedPost = userValidator.validatePost(id, token);

        Post post = postRepo.findById(id).orElseThrow(() -> {
            throw new CustomException("Post not found with id: " + id);
        });
        postRepo.delete(post);
        return modelMapper.map(post, PostDTO.class);
    }


    @Override
    public List<PostDTO> getPostsByUser(int userId){
        User user = userRepo.findById(userId).orElseThrow(() -> {
            throw new CustomException("User not found with id: " + userId);
        });

        List<Post> posts = postRepo.findAllByUser(user);
        return posts.stream().map(
            (e)->{
                return modelMapper.map(e,PostDTO.class);
            }
        ).collect(Collectors.toList());
    }



    @Override
    public PostDTO getPost(int post_id) {
        Post post = postRepo.findById(post_id).orElseThrow(() -> {
            throw new CustomException("Post not found with id :" + post_id);
        });
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepo.findAll();

        return posts.stream().map(
            (e)->{
                return modelMapper.map(e, PostDTO.class);
            }
        ).collect(Collectors.toList());
    }

}
