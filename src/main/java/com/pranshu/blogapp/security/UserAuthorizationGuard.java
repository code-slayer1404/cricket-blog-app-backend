package com.pranshu.blogapp.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.pranshu.blogapp.entity.Comment;
import com.pranshu.blogapp.entity.Post;
import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.exception.ResourceNotFoundException;
import com.pranshu.blogapp.exception.UnauthorizedException;
import com.pranshu.blogapp.repository.CommentRepo;
import com.pranshu.blogapp.repository.PostRepo;
import com.pranshu.blogapp.repository.UserRepo;
import com.pranshu.blogapp.util.JwtPrincipal;

@Component
public class UserAuthorizationGuard {
    
    private final UserRepo userRepo;
    private final PostRepo postRepo;
    private final CommentRepo commentRepo;

    UserAuthorizationGuard(UserRepo userRepo, PostRepo postRepo, CommentRepo commentRepo) {
        this.userRepo = userRepo;
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
    }

    public User getCurrentUser() {
        // MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // String username = myUserDetails.getUsername();
        JwtPrincipal jwtPrincipal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwtPrincipal.username();
        return userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        // return userRepo.getReferenceById(myUserDetails.getId());
    }

    public Post validatePost(int postId) {

        User currentUser = getCurrentUser();
        Post post = postRepo.findById(postId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Post not found with with id "+postId);
        });

        if (currentUser.getId() != post.getUser().getId()) {
            throw new UnauthorizedException("User is not the owner of the resource");
        }
        return post;
    }

    public Comment validateComment(int commentId) {

        User currentUser = getCurrentUser();
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Comment not found with with id " + commentId);
        });

        if (currentUser.getId() != comment.getUser().getId()) {
            throw new UnauthorizedException("Invalid user to perform this comment action");
        }
        return comment;
    }
}
