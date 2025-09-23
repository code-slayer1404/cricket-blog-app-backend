package com.pranshu.blogapp.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
// Handles authentication failures for protected endpoints in a stateless REST API.
// Sends a 401 Unauthorized response instead of redirecting, which is ideal for token-based authentication.
// Not required in session-based form login flows, where Spring Security redirects to the login page by default.

public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Access Denied!");
    }

}
