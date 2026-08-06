package com.pranshu.blogapp.security;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.pranshu.blogapp.constant.Role;
import com.pranshu.blogapp.util.MyUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTTokenHelper {

    // Base64 encoded secret key for signing JWT
    // public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    @Value("${jwt.secret}")
    public String SECRET ="qF3l9E8v1+7g1aN3kD1bZlS4R0v9K+oKxVtYH5z7eXc=";

    /**
     * Generates a JWT token for the given user name.
     * 
     * @param userName The name of the user
     * @return The generated JWT token
     */
    public String generateToken(Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails)authentication.getPrincipal();

        String userName = userDetails.getUsername();
        Map<String, Object> claims = new HashMap<>(); // Initialize claims map
        claims.put("id", userDetails.getId());
        claims.put("name", userDetails.getName());
        claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        System.out.println("claims before building: "+claims);
        return createToken(claims, userName); // Create and return token
    }

    /**
     * Creates a JWT token with the given claims and user name.
     * 
     * @param claims   The claims to include in the token
     * @param userName The name of the user
     * @return The generated JWT token
     */
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder() // Create JWT builder
                .setClaims(claims) // Set claims
                .setIssuer("self")
                .setSubject(userName) // Set subject
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set issue time
                .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 60)) // Set expiration time 10 hours
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
     * @param token The JWT token
     * @return The user name from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration time from the given token.
     * 
     * @param token The JWT token
     * @return The expiration time from the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Claims::getExpiration is similar to (claims) -> claims.getExpiration()
        // return extractAllClaims(token).getExpiration(); // simpler
    }

    /*
        untested
    */
    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        // List<String> roles2 = (List<String>)extractClaim(token,claims->{
        //         return claims.get("roles",List.class);
        //     });
        @SuppressWarnings("unchecked")
        List<String> roles = extractAllClaims(token).get("roles",List.class);
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }

    /*
        untested
    */
    public String extractName(String token) {

        // return extractClaim( token,claims->{
        //     return claims.get("name",String.class);
        // });
        String name  = extractAllClaims(token).get("name",String.class);
        return name;
    }
    
    /*
     * untested
     */
    public Integer extractId(String token) {

        // return extractClaim( token,claims->{
        //     return claims.get("id",Integer.class);
        // });
        Integer id  = extractAllClaims(token).get("id",Integer.class);
        return id;
    }

    /**
     * Extracts a claim from the given token using the provided claims resolver.
     * 
     * @param token          The JWT token
     * @param claimsResolver The claims resolver to use
     * @return The extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Extract all claims
        return claimsResolver.apply(claims); // claims.getExpiration/getSubject/... whomever calls it
    }

    /**
     * Extracts all claims from the given token.
     * This is what actually validates the token,
     * though its hard to see why as we cant he how
     * its called on calling validateToken(String, UserDetails),
     * if we are not careful
     * 
     * @param token The JWT token
     * @return The extracted claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey()) // Set signing key
                .build()
                .parseClaimsJws(token) // Parse token VVIP
                .getBody(); // Get claims
    }

    /**
     * Checks if the given token is expired.
     * 
     * @param token The JWT token
     * @return True if the token is expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Check expiration time
    }

    /**
     * Validates the given token against expiry and  the provided user details.
     * 
     * Safety-first approach:
     * - The username is extracted from the token, and then the corresponding
     * UserDetails (in filter)
     * is loaded from the system (e.g., database or in-memory store) in the filter.
     * - This check ensures that the username in the token actually matches the
     * loaded UserDetails.
     * Even though the UserDetails comes from the token's username, this provides an
     * extra safeguard as in some apps username may be allowed to change or
     * against potential mismatches, tampering, or future changes (e.g.,
     * multi-tenant apps,
     * token subject changes, or custom claim mappings).
     * - Also ensures the token has not expired.
     * 
     * @param token       The JWT token
     * @param userDetails The UserDetails object corresponding to the username in
     *                    the token
     * @return True if the token is valid, false otherwise
     */

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token); // Extract username
            return (username.equals(userDetails.getUsername()) // Check username. (username maybe changeble in some apps)
                    && !isTokenExpired(token)); // Check expiration
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean validateTokenStateless(String token) {
        try {
            return (!isTokenExpired(token)); // Check expiration
        } catch (Exception e) {
            return false;
        }
    }

    private String generateTokenTest(String username) { // just for test
        Map<String, Object> claims = new HashMap<>(); // Initialize claims map
        List<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(Role.ROLE_USER.name()),
            new SimpleGrantedAuthority(Role.ROLE_ADMIN.name())
        );
        claims.put("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList());
        claims.put("id", 14);
        claims.put("name", "Pranshu");
        return createToken(claims, username); // Create and return token
    }

    public static void main(String[] args) {
        JWTTokenHelper jwtTokenHelper = new JWTTokenHelper();
        String token  = jwtTokenHelper.generateTokenTest("pranshu@test");
        Collection<? extends GrantedAuthority> authorities = jwtTokenHelper.extractAuthorities(token);
        System.out.println(token);
        System.out.println(jwtTokenHelper.extractId(token));
        System.out.println(jwtTokenHelper.extractName(token));
        System.out.println(jwtTokenHelper.extractUsername(token));
        System.out.println(jwtTokenHelper.extractAllClaims(token).getIssuedAt());
        System.out.println(jwtTokenHelper.extractExpiration(token));
        authorities.forEach(System.out::println);

    }

}
/*
 * the `extractClaim` abstraction in your `JWTTokenHelper` looks a
 * bit “extra” at first glance. Let’s break down why it’s there and whether it’s
 * actually useful.
 * 
 * ---
 * 
 * ## 🔹 Direct approach (no abstraction)
 * You could absolutely write:
 * 
 * ```java
 * public String extractUsername(String token) {
 * return extractAllClaims(token).getSubject();
 * }
 * 
 * public Date extractExpiration(String token) {
 * return extractAllClaims(token).getExpiration();
 * }
 * ```
 * 
 * This is **simpler** and avoids the indirection. For two claims (`subject`,
 * `expiration`), the `extractClaim` method looks like boilerplate.
 * 
 * ---
 * 
 * ## 🔹 Why `extractClaim` exists
 * The author is trying to generalize claim extraction:
 * 
 * ```java
 * public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
 * final Claims claims = extractAllClaims(token);
 * return claimsResolver.apply(claims);
 * }
 * ```
 * 
 * - **Reusability**: Instead of writing a new method for every claim, you can
 * pass a resolver like `Claims::getIssuer`, `Claims::getId`, or even a custom
 * lambda `(c -> c.get("roles", List.class))`.
 * - **Consistency**: All claim extraction flows through one method, so if you
 * ever change how you parse claims (e.g., add logging, error handling,
 * auditing), you only change it in one place.
 * - **Flexibility**: Works for both standard claims (`sub`, `exp`) and custom
 * claims (`roles`, `tenantId`, etc.) without duplicating code.
 * 
 * ---
 * 
 * ## ⚡ Trade‑off
 * - For **basic username/expiration only**, the abstraction is arguably
 * overkill.
 * - For **apps with multiple custom claims**, `extractClaim` becomes valuable
 * because you don’t have to keep writing `extractAllClaims(token).getXxx()`
 * everywhere.
 * 
 * ---
 * 
 * ## 🚀 Bottom line
 * - If your app only ever needs `subject` and `expiration`, you can simplify
 * and inline `extractAllClaims().getSubject()` / `.getExpiration()`.
 * - If you expect to pull many different claims (especially custom ones),
 * `extractClaim` is a clean, reusable abstraction that avoids duplication and
 * centralizes claim parsing logic.
 * 
 * ---
 */