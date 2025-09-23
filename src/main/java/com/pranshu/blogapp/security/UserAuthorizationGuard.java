package com.pranshu.blogapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.pranshu.blogapp.entity.Comment;
import com.pranshu.blogapp.entity.Post;
import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.exception.CustomException;
import com.pranshu.blogapp.repository.CommentRepo;
import com.pranshu.blogapp.repository.PostRepo;
import com.pranshu.blogapp.repository.UserRepo;
import com.pranshu.blogapp.util.MyUserDetails;

@Component
public class UserAuthorizationGuard {
    
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private CommentRepo commentRepo;

    private User getCurrentUser() {
        String username = ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        return userRepo.findByUsername(username).orElseThrow(() -> new CustomException("Authenticated user not found"));
    }

    public Post validatePost(int postId) {

        User currentUser = getCurrentUser();
        Post post = postRepo.findById(postId).orElseThrow(() -> {
            throw new CustomException("Something went wrong while validating post! Could not fetch/find post!");
        });

        if (currentUser.getId() != post.getUser().getId()) {
            throw new CustomException("Invalid user");
        }
        return post;
    }

    public Comment validateComment(int commentId) {

        User currentUser = getCurrentUser();
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> {
            throw new CustomException("Something went wrong while validating comment! Could not fetch/find comment!");
        });

        if (currentUser.getId() != comment.getUser().getId()) {
            throw new CustomException("Invalid user to perform this comment action");
        }
        return comment;
    }
}
