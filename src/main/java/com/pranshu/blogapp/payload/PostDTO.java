package com.pranshu.blogapp.payload;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private int id;
    private String title;
    private String content;
    private Date date;
}

