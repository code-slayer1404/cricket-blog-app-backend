package com.pranshu.blogapp.service;



import com.pranshu.blogapp.payload.CommentDTO;
import com.pranshu.blogapp.payload.PagedResponse;

public interface CommentService {

    public CommentDTO addComment(CommentDTO commentDTO,int postId,String token);

    public CommentDTO updateComment(int comment_id, CommentDTO commentDTO,String token);

    public CommentDTO deleteComment(int comment_id,String token);

    public CommentDTO getComment(int commentId);

    public PagedResponse<CommentDTO> getCommentsByPost(int postId,int pageNumber);
}
