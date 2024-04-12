package com.pranshu.blogapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pranshu.blogapp.entity.Comment;
import com.pranshu.blogapp.entity.Post;
import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.exception.CustomException;
import com.pranshu.blogapp.repository.CommentRepo;
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
    @Autowired
    private CommentRepo commentRepo;

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
            throw new CustomException("Something went wrong while validating post! Could not fetch/find user!");
        });
        Post currentPost = postRepo.findById(postId).orElseThrow(() -> {
            throw new CustomException("Something went wrong while validating post! Could not fetch/find post!");
        });

        if (currentUser.getId() != currentPost.getUser().getId()) {
            throw new CustomException("Invalid user");
        }
        return currentPost;
    }

    public Comment validateComment(int commentId,String token){

        String username = jwtTokenHelper.extractUsername(token);
        User currentUser = userRepo.findByUsername(username).orElseThrow(() -> {
            throw new CustomException("Something went wrong while validating comment! Could not fetch/find user!");
        });

        Comment currentComment = commentRepo.findById(commentId).orElseThrow(() -> {
            throw new CustomException("Something went wrong while validating comment! Could not fetch/find comment!");
        });

        if (currentUser.getId() != currentComment.getUser().getId()) {
            throw new CustomException("Invalid user to perform this comment action");
        }
        return currentComment;
    }
}
