package com.pranshu.blogapp.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.payload.UserDTO;
import com.pranshu.blogapp.repository.UserRepo;
import com.pranshu.blogapp.util.MyMapper;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestUserService {

    @Mock
    private UserRepo userRepo;

    @Mock
    private MyMapper myMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        System.out.println("I was executed");
        lenient().when(userRepo.save(any(User.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        }); // mocking userRepo.save(User);

        lenient().when(myMapper.toUserDTO(any(User.class))).thenAnswer(
                invocation -> {
                    User user = invocation.getArgument(0);
                    UserDTO userDTO = UserDTO.builder().id(user.getId()).name(user.getName())
                            .username(user.getUsername()).build();
                    return userDTO;
                }); // mocking myMapper.toUserDTO(User)

        lenient().when(myMapper.toUser(any(UserDTO.class))).thenAnswer(
                invocation -> {
                    UserDTO userDTO = invocation.getArgument(0);
                    return User.builder().id(userDTO.getId()).name(userDTO.getName()).username(userDTO.getUsername())
                            .password(userDTO.getPassword()).build();
                }); // mocking myMapper.toUser(UserDTO)

    }

    // this method is actually not important for our app. we plan to remove it
    @Test
    public void UserService_addUser_UserDTO() {
        UserDTO userDTO = UserDTO.builder().id(1).name("user1").username("username1").password("pass1").build();

        UserDTO addedUserDTO = userService.addUser(userDTO);

        assertThat(addedUserDTO.getId()).isGreaterThan(0);

    }


    @Test
    public void userService_updateUser_UserDTO() {
        User mockUser = User.builder().id(1).name("test").username("test_username").password("test123").build();

        when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));

        int id = 1;
        UserDTO userDTO = UserDTO.builder().name("new_name").username("new_username").build();

        UserDTO updatedUser = userService.updateUser(userDTO, id);

        assertThat(updatedUser.getName()).isEqualTo(userDTO.getName());
        assertThat(updatedUser.getUsername()).isEqualTo(userDTO.getUsername());
    }

    @Test
    public void userService_deleteUser_UserDTO(){
        int user_id = 1;
        User mockUser = User.builder().id(user_id).name("test").username("test_username").password("test123").build();

        when(userRepo.findById(user_id)).thenReturn(Optional.of(mockUser));
        // when(userRepo.delete(mockUser)).thenReturn(null);


        userService.delete(user_id);

        verify(userRepo,times(1)).findById(user_id);
        verify(userRepo,times(1)).delete(mockUser);
        verify(myMapper,times(1)).toUserDTO(mockUser);
    }

    @Test
    public void userService_getUser_UserDTO(){
        int user_id = 1;
        User mockUser = User.builder().id(user_id).name("test").username("test_username").password("test123").build();

        when(userRepo.findById(user_id)).thenReturn(Optional.of(mockUser));

        UserDTO retrivedUser = userService.getUser(user_id);

        assertThat(retrivedUser).isNotNull();
        assertThat(retrivedUser.getId()).isEqualTo(user_id);
    }

    @Test
    public void userService_getAllUsers_ListOfUserDTO(){
        List<User> users = new ArrayList<>();
        User mockUser1 = User.builder().id(1).name("test1").username("test_username1").password("test123").build();
        User mockUser2 = User.builder().id(2).name("test2").username("test_username2").password("test456").build();
        User mockUser3 = User.builder().id(3).name("test3").username("test_username3").password("test789").build();

        users.add(mockUser1);
        users.add(mockUser2);
        users.add(mockUser3);

        when(userRepo.findAll()).thenReturn(users);

        List<UserDTO> retrievedUserDTOs = userService.getAllUsers();

        assertThat(retrievedUserDTOs.size()).isEqualTo(users.size());
    }


}
