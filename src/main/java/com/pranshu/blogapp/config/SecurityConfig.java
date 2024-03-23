package com.pranshu.blogapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.pranshu.blogapp.security.JWTAuthenticationEntryPoint;
import com.pranshu.blogapp.security.JWTAuthenticationFilter;
import com.pranshu.blogapp.util.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Bean
    public UserDetailsService getUserDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> {
            c.disable();
        }).authorizeHttpRequests(authz -> {
            authz.anyRequest().authenticated();
        }).exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthenticationEntryPoint)).sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        

        return http.build();
    }

    
}
/*
 * Yes, you're correct. `Customizer` is a functional interface in Spring
 * Security¹. It's a callback interface that accepts a single input argument and
 * returns no result¹.
 * 
 * The `Customizer` interface can be used with various methods in Spring
 * Security, not just `httpBasic`¹. The `withDefaults()` method of `Customizer`
 * returns a `Customizer` that does not alter the input argument¹, which
 * effectively applies the default settings¹.
 * 
 * So, you can use `Customizer.withDefaults()` with other methods in Spring
 * Security to apply the default settings¹.
 * 
 * 
 * 
 */