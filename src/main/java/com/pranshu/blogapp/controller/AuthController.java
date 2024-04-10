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


    /**
     * This is the login endpoint of the API. It accepts a JSON payload
     * containing a username and password, and attempts to authenticate
     * the user using the Spring Security AuthenticationManager.
     *
     * If the authentication is successful, it generates a JSON Web Token
     * (JWT) using the provided username, and maps the authenticated user
     * to a UserDTO object using the ModelMapper. It then creates a
     * JWTAuthResponse object containing the token and userDTO, and
     * returns this as a ResponseEntity with a 200 HTTP status code.
     *
     * If the authentication fails, it returns a ResponseEntity with a 403
     * HTTP status code.
     *
     * @param request The JSON payload containing the username and password.
     * @return A ResponseEntity containing a JWTAuthResponse object if the
     *         authentication was successful, or an HTTP 403 status code if not.
     */
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody JWTAuthRequest request) {
        // Attempt to authenticate the user using their provided credentials
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // If the authentication was successful
        if(authentication.isAuthenticated()){
            // Generate a JWT using the provided username
            String token = jwtTokenHelper.generateToken(request.getUsername());

            // Map the authenticated user to a UserDTO object
            User user = userRepo.findByUsername(request.getUsername()).orElseGet(null);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);

            // Create a JWTAuthResponse object containing the token and userDTO
            JWTAuthResponse response = new JWTAuthResponse();
            response.setToken(token);
            response.setUserDTO(userDTO);

            // Return the JWTAuthResponse as a ResponseEntity with a 200 HTTP status code
            return new ResponseEntity<JWTAuthResponse>(response,HttpStatus.OK);
        }
        // If the authentication failed, return a ResponseEntity with a 403 HTTP status code
        else{
            return new ResponseEntity<JWTAuthResponse>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO){
        UserDTO registeredUser = userService.registerUser(userDTO);
        return ResponseEntity.of(Optional.ofNullable(registeredUser));
    }

}
