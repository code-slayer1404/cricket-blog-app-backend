package com.pranshu.blogapp.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;

    private JWTTokenHelper jwtTokenHelper;

    public JWTAuthenticationFilter(UserDetailsService userDetailsService,JWTTokenHelper jwtTokenHelper){
        this.userDetailsService = userDetailsService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        /*
         * Step 1: Extract the Authorization Header from the request
         * The Authorization Header should contain a Bearer Token, which is a string starting with "Bearer ".
         */
        String authHeader = request.getHeader("Authorization");

        /*
         * Step 2: Extract the Token from the Authorization Header
         * If the Authorization Header exists and starts with "Bearer ", the token is the substring after "Bearer ".
         */
        String token = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
        }

        /*
         * Step 3: Extract the username from the Token
         * If we have a token, we can extract the username from it using the JWTTokenHelper.
         */
        String username = null;
        if(token!=null){
            username = jwtTokenHelper.extractUsername(token);
        }

        /*
         * Step 4: Load the userDetails for the username using the UserDetailsService
         * If we have a username and there is no Authentication in the Security Context, we load the user details.
         */
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            /*
             * Step 5: Validate the Token
             * We validate the token using the JWTTokenHelper and the userDetails.
             */
            if(jwtTokenHelper.validateToken(token, userDetails)){

                /*
                 * Step 6: Create a UsernamePasswordAuthenticationToken
                 * We create a UsernamePasswordAuthenticationToken with the user details, null password, and the user's authorities.
                 */
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());

                /*
                 * Step 7: Set the details of the Authentication Token
                 * We set the details of the Authentication Token with the details of the request.
                 */
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                /*
                 * Step 8: Set the Authentication in the Security Context
                 * We set the Authentication in the Security Context with the UsernamePasswordAuthenticationToken.
                 */
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        /*
         * Step 9: Continue the Filter Chain
         * We continue the Filter Chain with the request and response.
         */
        filterChain.doFilter(request, response);
    }

}
