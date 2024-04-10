package com.pranshu.blogapp.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTTokenHelper {

    // Base64 encoded secret key for signing JWT
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    /**
     * Generates a JWT token for the given user name.
     * 
     * @param  userName	The name of the user
     * @return            The generated JWT token
     */
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>(); // Initialize claims map
        return createToken(claims, userName); // Create and return token
    }

    /**
     * Creates a JWT token with the given claims and user name.
     * 
     * @param  claims    The claims to include in the token
     * @param  userName  The name of the user
     * @return           The generated JWT token
     */
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder() // Create JWT builder
                .setClaims(claims) // Set claims
                .setSubject(userName) // Set subject
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set issue time
                .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 60)) // Set expiration time
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact(); // Sign and compact token
    }

    /**
     * Returns the signing key used to sign JWT tokens.
     * 
     * @return The signing key
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET); // Decode secret key
        return Keys.hmacShaKeyFor(keyBytes); // Generate signing key
    }

    /**
     * Extracts the user name from the given token.
     * 
     * @param  token The JWT token
     * @return       The user name from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration time from the given token.
     * 
     * @param  token The JWT token
     * @return       The expiration time from the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a claim from the given token using the provided claims resolver.
     * 
     * @param  token         The JWT token
     * @param  claimsResolver The claims resolver to use
     * @return               The extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Extract all claims
        return claimsResolver.apply(claims); // Apply resolver and return claim
    }

    /**
     * Extracts all claims from the given token.
     * 
     * @param  token The JWT token
     * @return       The extracted claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey()) // Set signing key
                .build()
                .parseClaimsJws(token) // Parse token
                .getBody(); // Get claims
    }

    /**
     * Checks if the given token is expired.
     * 
     * @param  token The JWT token
     * @return       True if the token is expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Check expiration time
    }

    /**
     * Validates the given token against the provided user details.
     * 
     * @param  token      The JWT token
     * @param  userDetails The user details to validate against
     * @return            True if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Extract username
        return (username.equals(userDetails.getUsername()) // Check username
                && !isTokenExpired(token)); // Check expiration
    }
}
