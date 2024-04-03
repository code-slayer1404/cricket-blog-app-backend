package com.pranshu.blogapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.payload.UserDTO;
import com.pranshu.blogapp.repository.UserRepo;
import com.pranshu.blogapp.util.MyMapper;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MyMapper myMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        User user = myMapper.toUser(userDTO);
        user.setRoles(new ArrayList<>());
        user.getRoles().add("USER"); // check
        User savedUser = userRepo.save(user);
        return myMapper.toUserDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, int user_id) {
        User user = userRepo.findById(user_id).orElseThrow();

        user.setName(userDTO.getName());
        // user.setPassword(userDTO.getPassword());
        user.setUsername(userDTO.getUsername());

        User savedUser = userRepo.save(user);
        return myMapper.toUserDTO(savedUser);
    }

    @Override
    public UserDTO delete(int id) {
        User user = userRepo.findById(id).orElseThrow();
        userRepo.delete(user);
        return myMapper.toUserDTO(user);
    }

    @Override
    public UserDTO getUser(int id) {
        User user = userRepo.findById(id).orElseThrow();
        return myMapper.toUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserDTO> usersDTO = users.stream().map(
                (e) -> {
                    return myMapper.toUserDTO(e);
                }).collect(Collectors.toList());
        // List<UserDTO> usersDTO = users.stream().map(
        // (e)->{
        // return myMapper.toUserDTO(e);
        // }
        // ).toList();

        return usersDTO;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        try {
            User user = myMapper.toUser(userDTO);
            user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            user.getRoles().add("USER");
            User savedUser = userRepo.save(user);
            return myMapper.toUserDTO(savedUser);
        } catch (Exception e) {
            System.out.println("user cannot be registered! try with a different email");
            return null;
        }
    }

}
