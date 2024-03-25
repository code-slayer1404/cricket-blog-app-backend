package com.pranshu.blogapp.controller;

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

import com.pranshu.blogapp.payload.JWTAuthRequest;
import com.pranshu.blogapp.payload.JWTAuthResponse;
import com.pranshu.blogapp.security.JWTTokenHelper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JWTTokenHelper jwtTokenHelper;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> createToken(@RequestBody JWTAuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(authentication.isAuthenticated()){
            String token = jwtTokenHelper.generateToken(request.getUsername());
            JWTAuthResponse response = new JWTAuthResponse();
            response.setToken(token);

            return new ResponseEntity<JWTAuthResponse>(response,HttpStatus.OK);
        }else{
            return new ResponseEntity<JWTAuthResponse>(HttpStatus.FORBIDDEN);

        }
    }

}
