package com.pranshu.blogapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranshu.blogapp.TestConfig;
import com.pranshu.blogapp.config.SecurityConfig;
import com.pranshu.blogapp.payload.UserDTO;
import com.pranshu.blogapp.security.JWTAuthenticationFilter;
import com.pranshu.blogapp.service.UserService;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // @MockBean
    // BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void userController_addUser_UserDTO() throws Exception {
        UserDTO userDTO = UserDTO.builder().id(1).name("test").username("test_username").password("123").build();
        userDTO.getRoles().add("USER");

        when(userService.addUser(any(UserDTO.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk());

        response.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("test_username"));

    }

    @Test
    public void userController_updateUser_UserDTO() throws Exception {
        int user_id = 1;
        UserDTO userDTO = UserDTO.builder().id(user_id).name("test").username("test_username").password("123").build();
        userDTO.getRoles().add("USER");

        when(userService.updateUser(any(UserDTO.class), eq(user_id))).thenAnswer(
                invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(
                put("/users/{id}", user_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk());

        // response.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        // .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
        // .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("test_username"));

    }

    @Test
    public void testDeleteUser() throws Exception {
        int userId = 1;

        // Mocking userService.delete method
        UserDTO deletedUser = UserDTO.builder().id(userId).name("Test User").build();
        when(userService.delete(userId)).thenReturn(deletedUser);

        // Perform DELETE request
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(deletedUser.getId()));

        // Verify that userService.delete method was called with the correct userId
        verify(userService, times(1)).delete(userId);
    }

    @Test
    public void testGetUser() throws Exception {
        int userId = 1;

        // Mocking userService.getUser method
        UserDTO userDTO = UserDTO.builder().id(userId).name("Test User").build();
        when(userService.getUser(userId)).thenReturn(userDTO);

        // Perform GET request
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.name").value(userDTO.getName()));

        // Verify that userService.getUser method was called with the correct userId
        verify(userService, times(1)).getUser(userId);
    }


    @Test
    public void testGetAllUsers() throws Exception {
        // Mocking userService.getAllUsers method
        List<UserDTO> userDTOList = Arrays.asList(
                UserDTO.builder().id(1).name("User 1").build(),
                UserDTO.builder().id(2).name("User 2").build());
        when(userService.getAllUsers()).thenReturn(userDTOList);

        // Perform GET request
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("User 2"));

        // Verify that userService.getAllUsers method was called
        verify(userService, times(1)).getAllUsers();
    }

}
