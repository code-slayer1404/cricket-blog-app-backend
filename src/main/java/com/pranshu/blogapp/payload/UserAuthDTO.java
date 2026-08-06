package com.pranshu.blogapp.payload;

import java.util.HashSet;
import java.util.Set;

import com.pranshu.blogapp.constant.Role;

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
public class UserAuthDTO {

    private int id;
    private String name;
    private String username;
    private String password;
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

}
