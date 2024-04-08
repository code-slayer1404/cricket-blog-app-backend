package com.pranshu.blogapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pranshu.blogapp.entity.Post;
import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.exception.CustomException;
import com.pranshu.blogapp.repository.PostRepo;
import com.pranshu.blogapp.repository.UserRepo;

@Component
public class UserValidator {
    @Autowired
    private JWTTokenHelper jwtTokenHelper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PostRepo postRepo;

    public User validateUser(int userId,String token){
        String username = jwtTokenHelper.extractUsername(token);
        User currentUser = userRepo.findByUsername(username).orElseThrow(() -> {
            throw new CustomException("Something went wrong!");
        });

        if (currentUser.getId() != userId) {
            throw new CustomException("Invalid user");
        }
        return currentUser;
    }


    public Post validatePost(int postId,String token){
        String username = jwtTokenHelper.extractUsername(token);
        User currentUser = userRepo.findByUsername(username).orElseThrow(() -> {
            throw new CustomException("Something went wrong!");
        });
        Post currentPost = postRepo.findById(postId).orElseThrow(() -> {
            throw new CustomException("Something went wrong!");
        });

        if (currentUser.getId() != currentPost.getUser().getId()) {
            throw new CustomException("Invalid user");
        }
        return currentPost;
    }
}
