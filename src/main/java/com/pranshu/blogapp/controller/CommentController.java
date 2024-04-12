package com.pranshu.blogapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pranshu.blogapp.payload.CommentDTO;
import com.pranshu.blogapp.payload.PagedResponse;
import com.pranshu.blogapp.service.CommentService;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/posts/{post_id}/comments/{comment_id}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable("comment_id") int comment_id) {
        CommentDTO comment = commentService.getComment(comment_id);
        return ResponseEntity.ok(comment);
    }


    @PostMapping("/posts/{post_id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable("post_id") int post_id, @RequestBody CommentDTO commentDTO,@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        CommentDTO result = commentService.addComment(commentDTO, post_id, token);
        return new ResponseEntity<CommentDTO>(result, HttpStatus.CREATED);
    }

    @PutMapping("/posts/{post_id}/comments/{comment_id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable("comment_id") int comment_id, @RequestBody CommentDTO commentDTO,@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        CommentDTO result = commentService.updateComment(comment_id,commentDTO, token);
        return new ResponseEntity<CommentDTO>(result, HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/posts/{post_id}/comments/{comment_id}")
    public ResponseEntity<CommentDTO> deleteComment(@PathVariable("comment_id") int comment_id, @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        CommentDTO result = commentService.deleteComment(comment_id,token);
        return new ResponseEntity<CommentDTO>(result, HttpStatus.OK);
    }

    @GetMapping("/posts/{post_id}/comments")
    public ResponseEntity<PagedResponse<CommentDTO>> getAllCommentsByPost(@PathVariable("post_id") int post_id,@RequestParam(defaultValue = "1") int pageNumber) {
        
        PagedResponse<CommentDTO> result = commentService.getCommentsByPost(post_id, pageNumber);

        return ResponseEntity.ok(result);
    }
}
