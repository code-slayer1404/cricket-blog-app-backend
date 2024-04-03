package com.pranshu.blogapp.controller;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.payload.JWTAuthRequest;
import com.pranshu.blogapp.payload.JWTAuthResponse;
import com.pranshu.blogapp.payload.UserDTO;
import com.pranshu.blogapp.repository.UserRepo;
import com.pranshu.blogapp.security.JWTTokenHelper;
import com.pranshu.blogapp.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JWTTokenHelper jwtTokenHelper;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody JWTAuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(authentication.isAuthenticated()){
            String token = jwtTokenHelper.generateToken(request.getUsername());

            JWTAuthResponse response = new JWTAuthResponse();
            response.setToken(token);

            User user = userRepo.findByUsername(request.getUsername()).orElseGet(null);

            UserDTO userDTO = modelMapper.map(user, UserDTO.class);

            response.setUserDTO(userDTO);

            

            return new ResponseEntity<JWTAuthResponse>(response,HttpStatus.OK);
        }else{
            return new ResponseEntity<JWTAuthResponse>(HttpStatus.FORBIDDEN);

        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO){
        UserDTO registeredUser = userService.registerUser(userDTO);
        return ResponseEntity.of(Optional.ofNullable(registeredUser));
    }

}
