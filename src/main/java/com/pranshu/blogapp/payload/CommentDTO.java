package com.pranshu.blogapp.payload;

import java.util.Date;

import com.pranshu.blogapp.entity.Post;
import com.pranshu.blogapp.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDTO {

    private int id;
    private String content;
    private Date date;

    private User user;
    private Post post;
    
}
