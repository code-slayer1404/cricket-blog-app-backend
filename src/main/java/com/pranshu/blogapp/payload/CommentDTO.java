package com.pranshu.blogapp.payload;

import java.util.Date;

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

    private UserDTO user;
    private PostDTO post;
    
}
