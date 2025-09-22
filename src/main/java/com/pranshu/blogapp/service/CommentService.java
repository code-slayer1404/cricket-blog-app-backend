package com.pranshu.blogapp.service;



import com.pranshu.blogapp.payload.CommentDTO;
import com.pranshu.blogapp.payload.PagedResponse;

public interface CommentService {

    public CommentDTO addComment(CommentDTO commentDTO,int postId);

    public CommentDTO updateComment(int comment_id, CommentDTO commentDTO);

    public CommentDTO deleteComment(int comment_id);

    public CommentDTO getComment(int commentId);

    public PagedResponse<CommentDTO> getCommentsByPost(int postId,int pageNumber);
}
