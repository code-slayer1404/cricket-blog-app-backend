package com.pranshu.blogapp.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pranshu.blogapp.constant.AppIntegerConstants;
import com.pranshu.blogapp.entity.Comment;
import com.pranshu.blogapp.entity.Post;
import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.payload.CommentDTO;
import com.pranshu.blogapp.payload.PagedResponse;
import com.pranshu.blogapp.exception.CustomException;

import com.pranshu.blogapp.repository.CommentRepo;
import com.pranshu.blogapp.repository.PostRepo;
import com.pranshu.blogapp.repository.UserRepo;

import com.pranshu.blogapp.security.JWTTokenHelper;
import com.pranshu.blogapp.security.UserValidator;
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private JWTTokenHelper jwtTokenHelper;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Add a comment to the given post and return the saved comment DTO.
     *
     * @param  commentDTO  the comment data transfer object to be added
     * @param  postId       the id of the post to which the comment is added
     * @param  token        the authentication token of the current user
     * @return              the saved comment data transfer object
     */
    @Override
    public CommentDTO addComment(CommentDTO commentDTO, int postId, String token) {

        // fetching post by id
        Post post = postRepo.findById(postId).orElseThrow(() -> {
            throw new CustomException("Post not found with id: " + postId);
        });

        // extracting user from token
        String username = jwtTokenHelper.extractUsername(token);
        User currentUser = userRepo.findByUsername(username).orElseThrow(() -> {
            throw new CustomException("Error adding comment. User not found with username: " + username);
        });


        // setting up the comment to save
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setDate(new Date());
        comment.setUser(currentUser);
        comment.setPost(post);


        // also ading the comment to the post and user
        currentUser.getComments().add(comment);
        post.getComments().add(comment);

        // saving the comment, user and post
        Comment savedComment = commentRepo.save(comment);
        postRepo.save(post);
        userRepo.save(currentUser);

        CommentDTO savedCommentDTO =  modelMapper.map(savedComment, CommentDTO.class);
        return savedCommentDTO;

    }

    /**
     * Update a comment with new content and return the updated comment details.
     *
     * @param  comment_id    the ID of the comment to be updated
     * @param  commentDTO     the new content for the comment
     * @param  token         the authentication token
     * @return               the updated comment details as a CommentDTO object
     */
    @Override
    public CommentDTO updateComment(int comment_id, CommentDTO commentDTO, String token) {
        Comment comment = userValidator.validateComment(comment_id, token);

        comment.setContent(commentDTO.getContent());
        comment.setDate(new Date());

        Comment updatedComment = commentRepo.save(comment);

        return modelMapper.map(updatedComment, CommentDTO.class);
    }

    /**
     * Deletes a comment by its ID and returns the details of the deleted comment.
     *
     * @param  comment_id    the ID of the comment to be deleted
     * @param  token         the authentication token
     * @return               the details of the deleted comment as a CommentDTO object
     */
    @Override
    public CommentDTO deleteComment(int comment_id, String token) {
        // Validate the comment for deletion
        Comment comment = userValidator.validateComment(comment_id, token);

        // Delete the comment from the repository
        commentRepo.delete(comment);

        // Map the deleted comment to CommentDTO and return it
        return modelMapper.map(comment, CommentDTO.class);
    }


    /**
     * Retrieves a paged response of commentDTO objects related to a specific post.
     *
     * @param  postId       the ID of the post
     * @param  pageNumber   the page number
     * @return              a paged response containing commentDTO objects
     * @throws CustomException  if the post with the given ID is not found
     */
    @Override
    public PagedResponse<CommentDTO> getCommentsByPost(int postId, int pageNumber) {
        // Setup the pagination
        // careful its zero based page number
        Pageable pageable = PageRequest.of(pageNumber - 1, AppIntegerConstants.PAGE_SIZE.getValue());

        // Find the post by ID
        Post post = postRepo.findById(postId).orElseThrow(() -> {
            throw new CustomException("Post not found with id: " + postId);
        });

        // Fetch the comments related to the post
        Page<Comment> comments = commentRepo.findAllByPost(post, pageable);

        // Extract the pagination details
        int totalPages = comments.getTotalPages();
        int currentPage = comments.getNumber()+1;

        // Map the comments to CommentDTO objects
        List<CommentDTO> content = comments.getContent().stream().map(
            (comment) -> modelMapper.map(comment, CommentDTO.class)
        ).collect(Collectors.toList());

        // Create the paged response
        PagedResponse<CommentDTO> pagedResponse = new PagedResponse<>(currentPage, totalPages, content);

        return pagedResponse;
    }

    @Override
    public CommentDTO getComment(int commentId) {
        // Fetch the comment by ID
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> {
            throw new CustomException("Comment not found with id: " + commentId);
        });

        // Map the comment to CommentDTO object
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);

        // Return the CommentDTO object
        return commentDTO;
    }

}
