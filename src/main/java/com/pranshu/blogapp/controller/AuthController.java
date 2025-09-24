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
        // Attempt to authenticate the user using their provided credentials (we need to do this as its not form login where its automatically handled internally)
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
        // no need to save authentication object in spring security context here. that was not job of this method and even if we did no pint as it will immediately be wiped as soon as response is returned
    }



    
    //  Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
    //  SecurityContextHolder.getContext().setAuthentication(authentication);
    //  
    //  //vs
    //  
    //  UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    //  UsernamePasswordAuthenticationToken authToken = newUsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    //  authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    //  SecurityContextHolder.getContext().setAuthentication(authToken); 
    //  
    //  //whats the difference?
    //  
    //  in the first is spring automatically verifies username and password
    //  using authentication manager using a dao authentication provider.
    //  suitable for session based flows(not needed in form login).
    //  while the second one is more suitable for stateless jwt flows. It is
    //  manual and does not verifies username or passwor as security
    //  guarantee comes from token itself.
    //  is my understanding correct?
    //  
    //  ChatGPT said:
    //  
    //  Yes — your understanding is basically spot on, but let me refine it a
    //  little for clarity:
    //  
    //  Case 1: Using AuthenticationManager.authenticate(...)
    //  Authentication authentication = authenticationManager.authenticate(
    //  new UsernamePasswordAuthenticationToken(request.getUsername(),
    //  request.getPassword())
    //  );
    //  SecurityContextHolder.getContext().setAuthentication(authentication);
    //  
    //  
    //  Here you are delegating to Spring Security’s authentication
    //  mechanism.
    //  
    //  The AuthenticationManager (often backed by DaoAuthenticationProvider) will:
    //  
    //  Load the user via UserDetailsService.
    //  
    //  Verify the raw password against the stored password using the
    //  configured PasswordEncoder.
    //  
    //  Throw an exception if invalid.
    //  
    //  If successful, you get back a fully-authenticated Authentication
    //  object.
    //  
    //  ✅ This is the normal login flow (e.g., form login, session-based
    //  auth).
    //  
    //  The authentication step itself guarantees password verification.
    //  
    //  Case 2: Manually building an Authentication object
    //  UserDetails userDetails =
    //  userDetailsService.loadUserByUsername(username);
    //  
    //  UsernamePasswordAuthenticationToken authToken =
    //  new UsernamePasswordAuthenticationToken(userDetails, null,
    //  userDetails.getAuthorities());
    //  authToken.setDetails(new
    //  WebAuthenticationDetailsSource().buildDetails(request));
    //  
    //  SecurityContextHolder.getContext().setAuthentication(authToken);
    //  
    //  
    //  You are not verifying the password here.
    //  
    //  You’re manually putting an Authentication into the context, based on
    //  trusted information (like a valid JWT).(in jwt filter I am doing this only
    //  after token as been validated)
    //  
    //  The UserDetailsService is often used only to rehydrate user
    //  roles/permissions from the DB (or cache), not to validate
    //  credentials.
    //  
    //  ✅ This is the JWT/stateless flow:
    //  
    //  The JWT itself proves authenticity (signed token).
    //  
    //  Since password verification already happened at login (when the token
    //  was issued), no need to check it again.
    //  
    //  Security relies on the cryptographic verification of the token
    //  instead of username+password.
    //  
    //  🔑 Key Difference
    //  
    //  First approach = verification step included (username + password
    //  checked).
    //  
    //  Second approach = no verification step (you assume credentials are
    //  already proven by some external guarantee like a JWT).
    //  
    //  👉 So yes — your summary is correct:
    //  
    //  Case 1: session-based / login flows → Spring does the heavy lifting,
    //  checks credentials.
    //  
    //  Case 2: stateless/JWT flows → You trust the token and manually set
    //  authentication, no password check.
    //  
    //  Would you like me to also illustrate how the second flow typically
    //  looks inside a JWT filter (where you extract the token, validate it,
    //  and then set the context)? That would make the difference even
    //  clearer.

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO){
        UserDTO registeredUser = userService.registerUser(userDTO);
        return ResponseEntity.of(Optional.ofNullable(registeredUser));
    }

}
