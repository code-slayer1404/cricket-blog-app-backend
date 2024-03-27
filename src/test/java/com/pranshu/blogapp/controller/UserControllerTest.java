package com.pranshu.blogapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranshu.blogapp.TestConfig;
import com.pranshu.blogapp.config.SecurityConfig;
import com.pranshu.blogapp.payload.UserDTO;
import com.pranshu.blogapp.security.JWTAuthenticationFilter;
import com.pranshu.blogapp.service.UserService;


@WebMvcTest(controllers = UserController.class,excludeAutoConfiguration = SecurityConfig.class)
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
    public void userController_addUser_UserDTO() throws Exception{
        UserDTO userDTO = UserDTO.builder().id(1).name("test").username("test_username").password("123").build();
        userDTO.getRoles().add("USER");

        when(userService.addUser(any(UserDTO.class))).thenAnswer(
            invocation -> invocation.getArgument(0)
        );

        ResultActions response = mockMvc.perform(
            post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDTO))
            );

        response.andExpect(MockMvcResultMatchers.status().isOk());

        response.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("test_username"));

    }

}
