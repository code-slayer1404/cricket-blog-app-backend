package com.pranshu.blogapp.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.payload.UserDTO;
import com.pranshu.blogapp.repository.UserRepo;
import com.pranshu.blogapp.util.MyMapper;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestUserService {

    @Mock // will be inserted into the mock userService
    private UserRepo userRepo;

    @Mock
    private MyMapper myMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void init() {
        System.out.println("I was executed");

        // lenient because some tests may not call userRepo.save
        lenient().when(userRepo.save(any(User.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        }); // mocking userRepo.save(User);

        lenient().when(myMapper.toUserDTO(any(User.class))).thenAnswer(
                invocation -> {
                    User user = invocation.getArgument(0);
                    UserDTO userDTO = UserDTO.builder().id(user.getId()).name(user.getName())
                            .username(user.getUsername()).password(user.getPassword()).build();
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

    @Test
    public void userService_register_UserDTO(){
        UserDTO userDTO = UserDTO.builder().id(1).name("user1").username("username1").password("pass1").build();
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hashed_password");

        UserDTO registeresUserDTO = userService.registerUser(userDTO);

        assertThat(registeresUserDTO).isNotNull();
        assertThat(registeresUserDTO.getPassword()).isEqualTo("hashed_password");

    }


}



// when:

// Used for strict behavior.

// Example:

// java

// Copy when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));

// Why Here:
// You expect

// findById(1) to be called exactly once and return mockUser. This strict expectation helps ensure your test fails if findById(1) isn't called correctly.

// lenient().when:

// More forgiving, doesn’t fail the test if the method isn’t called exactly as specified.

// Example:

// java

// Copy
// lenient().when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
// Why Here: You set up general behavior for save and myMapper methods, but the test doesn’t rely strictly on whether these methods are called. It’s useful for methods that might be part of complex or optional interactions.

// Summary:
// Strict when: For interactions that must happen for the test to be valid.

// Lenient when: For interactions that can happen, but the test doesn’t fail if they don’t.

// This makes your tests robust yet flexible, ensuring critical interactions are checked while allowing flexibility in others. Got more testing insights to unlock? 🚀




// You
// lenient().when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
// what if I used just when here
// Copilot
// Using when instead of lenient().when for userRepo.save(any(User.class)).thenAnswer(invocation -> invocation.getArgument(0)) would make the test stricter. This means if userRepo.save isn't called exactly as stubbed, the test will fail.

// Example:
// Strict when:

// java

// Copy
// when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
// Behavior: If save isn’t called with a User argument at some point in your test, the test fails.

// When to Use:
// Use when: For critical interactions you expect to always happen. Ensures your test fails if the interaction doesn't occur as expected.

// Use lenient().when: For interactions that might be optional or less critical, where you don’t want the test to fail if the interaction doesn’t happen exactly as stubbed.

// So, for key, must-have interactions, stick with when. For more optional, forgiving scenarios, lenient().when does the trick. Neat, right? 🚀 Got more testing insights to explore?



// is it safe to say that if using when in BeforeEach and its intented to be used in other tests, we must use lenient when otherwise the test will fail?
// Copilot
// You've got it. If you're setting up stubs with when in BeforeEach, and those stubs might not be used in every test, switching to lenient().when is a smart move. This ensures your tests won't fail just because a particular method wasn't called. It’s all about making your tests robust yet flexible. Want to explore more testing nuances? 🚀