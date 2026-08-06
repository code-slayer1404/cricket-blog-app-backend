package com.pranshu.blogapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

import com.pranshu.blogapp.repository.UserRepo;
import com.pranshu.blogapp.security.JWTAuthenticationEntryPoint;
import com.pranshu.blogapp.security.JWTAuthenticationFilter;
import com.pranshu.blogapp.security.JWTTokenHelper;
import com.pranshu.blogapp.util.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepo userRepo;
    private final JWTTokenHelper jwtTokenHelper;
    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    SecurityConfig(UserRepo userRepo, JWTTokenHelper jwtTokenHelper, JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.userRepo = userRepo;
        this.jwtTokenHelper = jwtTokenHelper;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }
    

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new MyUserDetailsService(userRepo);
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter getJwtAuthenticationFilter(){
        // return new JWTAuthenticationFilter(getUserDetailsService(),jwtTokenHelper);
        return new JWTAuthenticationFilter(jwtTokenHelper);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> {
            c.disable();
        })
        .cors(c->c.configurationSource(getCorsConfigurationSource()))
        .authorizeHttpRequests(authz -> {
            authz.requestMatchers("/api/auth/**").permitAll().requestMatchers(HttpMethod.GET).permitAll().anyRequest().authenticated();
        }).exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        // This setup bypasses session-based login flows and relies entirely on token validation.
        // The .authenticated() method ensures that only authenticated users can access protected endpoints.
        // It does so by checking the presence of an Authentication object in the SecurityContext.
                
        http.addFilterBefore(getJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    // CORS (Cross-Origin Resource Sharing) is a browser-enforced security feature.
    // Browsers block frontend apps from calling APIs hosted on a different domain
    // unless the backend explicitly allows it.
    //
    // This backend configuration provides the rules (allowed origins, methods,
    // headers, and credential policies) that browsers check before letting a
    // frontend read API responses.
    //
    // In short: the backend tells the browser which domains are trusted to
    // communicate with this API, preventing unauthorized websites from accessing
    // user data through the browser.

    @Bean
    public CorsConfigurationSource getCorsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("https://code-slayer1404.github.io","http://blogappfrontendbucket.s3-website.ap-south-1.amazonaws.com","http://localhost:[5173]"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(1800L);
        

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return urlBasedCorsConfigurationSource;
    }

    
}