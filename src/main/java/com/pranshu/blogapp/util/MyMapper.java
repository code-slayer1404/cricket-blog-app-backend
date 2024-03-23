package com.pranshu.blogapp.util;

import org.springframework.stereotype.Component;

import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.payload.UserDTO;
@Component
public class MyMapper {
    public User toUser(UserDTO userDTO){
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        return user;
    }


    public UserDTO toUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }
}
