package com.pranshu.blogapp.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pranshu.blogapp.util.JwtPrincipal;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private JWTTokenHelper jwtTokenHelper;

    public JWTAuthenticationFilter(JWTTokenHelper jwtTokenHelper) {
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("Jwt Filter was executed");

        String authHeader = request.getHeader("Authorization");
        final String username;
        final String token;
        final Integer userId;
        final String name;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);
        username = jwtTokenHelper.extractUsername(token);
        userId = jwtTokenHelper.extractId(token);
        name = jwtTokenHelper.extractName(token);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            final Collection<? extends GrantedAuthority> authorities = jwtTokenHelper.extractAuthorities(token);
            JwtPrincipal jwtPrincipal = new JwtPrincipal(userId, name, username, authorities);

            if (jwtTokenHelper.validateTokenStateless(token)) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(jwtPrincipal,
                        null, jwtPrincipal.authorities()); // stateless

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}

// @Service
// public class JWTAuthenticationFilter extends OncePerRequestFilter {

// private UserDetailsService userDetailsService;

// private JWTTokenHelper jwtTokenHelper;

// public JWTAuthenticationFilter(UserDetailsService userDetailsService,
// JWTTokenHelper jwtTokenHelper) {
// this.userDetailsService = userDetailsService;
// this.jwtTokenHelper = jwtTokenHelper;
// }

// @Override
// protected void doFilterInternal(HttpServletRequest request,
// HttpServletResponse response, FilterChain filterChain)
// throws ServletException, IOException {

// System.out.println("Jwt Filter was executed");

// // Step 1: Extract the Authorization Header from the request
// String authHeader = request.getHeader("Authorization");
// final String username;
// final String token;

// // Step 2: Extract the Token from the Authorization Header, if not then
// proceed
// // further directly
// if (authHeader == null || !authHeader.startsWith("Bearer ")) {
// filterChain.doFilter(request, response);
// return;
// }

// // Step 3: Extract the username from the Token
// token = authHeader.substring(7);
// username = jwtTokenHelper.extractUsername(token);

// /*
// * Step 4: Load the userDetails for the username using the UserDetailsService
// * If we have a username and there is no Authentication in the Security
// Context,
// * we load the user details.
// */
// if (username != null &&
// SecurityContextHolder.getContext().getAuthentication() == null) {
// UserDetails userDetails = userDetailsService
// .loadUserByUsername(username); // not fully stateless in
// // principle.
// // Integer userId = jwtTokenHelper.extractId(token);
// // String name = jwtTokenHelper.extractName(token);
// // Collection<? extends GrantedAuthority> authorities =
// jwtTokenHelper.extractAuthorities(token);
// // JwtPrincipal jwtPrincipal = new
// JwtPrincipal(userId,name,username,authorities);
// /*
// * Step 5: Validate the Token
// * We validate the token using the JWTTokenHelper and the userDetails.
// */
// if (jwtTokenHelper.validateToken(token, userDetails)) {

// /*
// * Step 6: Create a UsernamePasswordAuthenticationToken
// * We create a UsernamePasswordAuthenticationToken with the user details, null
// * password, and the user's authorities.
// */
// UsernamePasswordAuthenticationToken authToken = new
// UsernamePasswordAuthenticationToken(userDetails,
// null, userDetails.getAuthorities()); // not stateless

// // to make the authentication fully stateless, instead of using UserDetails
// we
// // could just use username and roles exactracted from token. (but here roles
// are
// // ony in UserDetails and not in the JWT token making our approach pseudo
// // stateless)
// // but if roles were in JWT token, and we avoided database look up in this
// // filter, it would be a fully stateless approach, but it would cause stale
// // token issues and inability to immediately revocate access.
// // there are ways to handle those risks like dual tokens, redis ban list or
// // checking token versioning, etc. but here we avoid all these at the cost of
// // breaking full statelessness.

// // UsernamePasswordAuthenticationToken authToken = new
// // UsernamePasswordAuthenticationToken(userDetails,
// // null, jwtTokenHelper.extractAuthorities(token)); // stateless but not
// tested

// /*
// * Step 7: Set the details of the Authentication Token
// * We set the details of the Authentication Token with the details of the
// * request.
// */
// // Attach request-specific details (like remote IP, session ID) to the
// // authentication object.
// // In username/password flows this is done automatically by
// // AuthenticationManager,
// // but in JWT/manual authentication we need to set it explicitly if we want
// // those details.
// authToken.setDetails(new
// WebAuthenticationDetailsSource().buildDetails(request));

// /*
// * Step 8: Set the Authentication in the Security Context for each request
// * We set the Authentication in the Security Context with the
// * UsernamePasswordAuthenticationToken.
// */
// SecurityContextHolder.getContext().setAuthentication(authToken);
// }
// }

// /*
// * Step 9: Continue the Filter Chain
// * We continue the Filter Chain with the request and response.
// */
// filterChain.doFilter(request, response);
// }

// }









// Authentication authentication = authenticationManager.authenticate(new
// UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
// SecurityContextHolder.getContext().setAuthentication(authentication);
// this happens behind the scenes in spring boot default form login

// //vs
// here we first verify token then extract username from it, which is then used
// to load userDetails
// UserDetails userDetails = userDetailsService.loadUserByUsername(username);
// UsernamePasswordAuthenticationToken authToken =
// newUsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
// authToken.setDetails(new
// WebAuthenticationDetailsSource().buildDetails(request));
// SecurityContextHolder.getContext().setAuthentication(authToken);
//
// //whats the difference?
//
// in the first is spring automatically verifies username and password
// using authentication manager using a dao authentication provider.
// suitable for session based flows(not needed in form login).
// while the second one is more suitable for stateless jwt flows. It is
// manual and does not verifies username or passwor as security
// guarantee comes from token itself.
// is my understanding correct?
//
// ChatGPT said:
//
// Yes — your understanding is basically spot on, but let me refine it a
// little for clarity:
//
// Case 1: Using AuthenticationManager.authenticate(...)
// Authentication authentication = authenticationManager.authenticate(
// new UsernamePasswordAuthenticationToken(request.getUsername(),
// request.getPassword())
// );
// SecurityContextHolder.getContext().setAuthentication(authentication);
//
//
// Here you are delegating to Spring Security’s authentication
// mechanism.
//
// The AuthenticationManager (often backed by DaoAuthenticationProvider) will:
//
// Load the user via UserDetailsService.
//
// Verify the raw password against the stored password using the
// configured PasswordEncoder.
//
// Throw an exception if invalid.
//
// If successful, you get back a fully-authenticated Authentication
// object.
//
// ✅ This is the normal login flow (e.g., form login, session-based
// auth).
//
// The authentication step itself guarantees password verification.
//
// Case 2: Manually building an Authentication object
// UserDetails userDetails =
// userDetailsService.loadUserByUsername(username);
//
// UsernamePasswordAuthenticationToken authToken =
// new UsernamePasswordAuthenticationToken(userDetails, null,
// userDetails.getAuthorities());
// authToken.setDetails(new
// WebAuthenticationDetailsSource().buildDetails(request));
//
// SecurityContextHolder.getContext().setAuthentication(authToken);
//
//
// You are not verifying the password here.
//
// You’re manually putting an Authentication into the context, based on
// trusted information (like a valid JWT).(in jwt filter I am doing this only
// after token as been validated)
//
// The UserDetailsService is often used only to rehydrate user
// roles/permissions from the DB (or cache), not to validate
// credentials.
//
// ✅ This is the JWT/stateless flow:
//
// The JWT itself proves authenticity (signed token).
//
// Since password verification already happened at login (when the token
// was issued), no need to check it again.
//
// Security relies on the cryptographic verification of the token
// instead of username+password.
//
// 🔑 Key Difference
//
// First approach = verification step included (username + password
// checked).
//
// Second approach = no verification step (you assume credentials are
// already proven by some external guarantee like a JWT).
//
// 👉 So yes — your summary is correct:
//
// Case 1: session-based / login flows → Spring does the heavy lifting,
// checks credentials.
//
// Case 2: stateless/JWT flows → You trust the token and manually set
// authentication, no password check.
//
