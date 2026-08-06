package com.pranshu.blogapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pranshu.blogapp.constant.Role;
import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.payload.JWTAuthRequest;
import com.pranshu.blogapp.payload.UserAuthDTO;
import com.pranshu.blogapp.payload.UserDTO;
import com.pranshu.blogapp.repository.UserRepo;
import com.pranshu.blogapp.util.MyMapper;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final MyMapper myMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    UserServiceImpl(UserRepo userRepo, MyMapper myMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.myMapper = myMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDTO addUser(UserAuthDTO userDTO) {
        User user = myMapper.toUser(userDTO);
        user.getRoles().add(Role.ROLE_USER); // check
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword())); // encode password
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

    // @Override
    // public UserDTO registerUser(UserAuthDTO userDTO) {
    //     try {
    //         User user = myMapper.toUser(userDTO);
    //         // System.out.println(user.getUsername() + user.getPassword());
    //         user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
    //         user.getRoles().add(Role.ROLE_USER);
    //         User savedUser = userRepo.save(user);
    //         return myMapper.toUserDTO(savedUser);
    //     } catch (Exception e) {
    //         System.out.println(e.getMessage());
    //         System.out.println("user cannot be registered! try with a different email");
    //         return null;
    //     }
    // }

    @Override
    public UserDTO registerUser(JWTAuthRequest jwtAuthRequest) {
        try {
            User user = User.builder()
            .username(jwtAuthRequest.getUsername())
            .password(bCryptPasswordEncoder.encode(jwtAuthRequest.getPassword()))
            .build();

            user.getRoles().add(Role.ROLE_USER);
            
            User savedUser = userRepo.save(user);
            return myMapper.toUserDTO(savedUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("user cannot be registered! try with a different email");
            return null;
        }
    }

}
