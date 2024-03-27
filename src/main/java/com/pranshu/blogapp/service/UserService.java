package com.pranshu.blogapp.service;

import java.util.List;

import com.pranshu.blogapp.payload.UserDTO;

public interface UserService {
    public UserDTO registerUser(UserDTO userDTO);
    public UserDTO addUser(UserDTO userDTO); // just for testing , no real significance
    public UserDTO updateUser(UserDTO userDTO,int user_id);
    public UserDTO delete(int id);
    public UserDTO getUser(int id);
    public List<UserDTO> getAllUsers();
}
