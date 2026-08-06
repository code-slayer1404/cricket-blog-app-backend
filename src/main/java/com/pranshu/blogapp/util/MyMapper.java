package com.pranshu.blogapp.util;

import org.springframework.stereotype.Component;

import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.payload.UserAuthDTO;
import com.pranshu.blogapp.payload.UserDTO;

@Component
public class MyMapper {
    public User toUser(UserDTO userDTO){

        User user = User.builder()
                        .id(userDTO.getId())
                        .name(userDTO.getName())
                        .username(userDTO.getUsername())
                        .build();

        return user;
    }

    public User toUser(UserAuthDTO userDTO){

        User user = User.builder()
                        .id(userDTO.getId())
                        .name(userDTO.getName())
                        .username(userDTO.getUsername())
                        .password(userDTO.getPassword())
                        .roles(userDTO.getRoles())
                        .build();

        return user;
    }

    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }
}
