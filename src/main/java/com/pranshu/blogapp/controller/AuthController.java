package com.pranshu.blogapp.controller;

import java.util.Optional;

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
import com.pranshu.blogapp.payload.UserDTO;
import com.pranshu.blogapp.security.JWTTokenHelper;
import com.pranshu.blogapp.service.UserService;
import com.pranshu.blogapp.util.MyUserDetails;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JWTTokenHelper jwtTokenHelper;
    private final AuthenticationManager authenticationManager;

    AuthController(UserService userService, JWTTokenHelper jwtTokenHelper,
            AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenHelper = jwtTokenHelper;
        this.authenticationManager = authenticationManager;
    }

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
        try {
            // Attempt to authenticate the user using their provided credentials (we need to
            // do this as its not form login where its automatically handled internally)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));


            // Generate a JWT using the authentication
            String token = jwtTokenHelper.generateToken(authentication);

            // Map the authenticated user to a UserDTO object
            // authentication uses our MyUserDetailsService to get principal of MyUserDetails but in Object
            MyUserDetails myUserDetails = (MyUserDetails)authentication.getPrincipal();
            // here for first time we get MyUserDetails principle. Later we use JwtPrincipal
            
            UserDTO userDTO = UserDTO.builder()
                                    .id(myUserDetails.getId())
                                    .name(myUserDetails.getName())
                                    .username(myUserDetails.getUsername())
                                    .build();

            // Create a JWTAuthResponse object containing the token and userDTO
            JWTAuthResponse response = new JWTAuthResponse();
            response.setToken(token);
            response.setUserDTO(userDTO);

            // Return the JWTAuthResponse as a ResponseEntity with a 200 HTTP status code
            return new ResponseEntity<JWTAuthResponse>(response, HttpStatus.OK);
        }
        // If the authentication failed, return a ResponseEntity with a 403 HTTP status
        // code
        catch (Exception e) {
            return new ResponseEntity<JWTAuthResponse>(HttpStatus.FORBIDDEN);
        }
    }


    // cant give name to user
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register1(@RequestBody JWTAuthRequest jwtAuthRequest) {
        UserDTO registeredUser = userService.registerUser(jwtAuthRequest);
        return ResponseEntity.of(Optional.ofNullable(registeredUser));
    }

}
