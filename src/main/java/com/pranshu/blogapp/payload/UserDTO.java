package com.pranshu.blogapp.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {

    private int id;

    private String name;
    private String username;
    private String password;


}
