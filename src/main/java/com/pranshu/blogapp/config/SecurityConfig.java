package com.pranshu.blogapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

import org.springframework.beans.factory.annotation.Autowired;

import com.pranshu.blogapp.security.JWTAuthenticationEntryPoint;
import com.pranshu.blogapp.security.JWTAuthenticationFilter;
import com.pranshu.blogapp.security.JWTTokenHelper;
import com.pranshu.blogapp.util.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;
    

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new MyUserDetailsService();
    }


    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter getJwtAuthenticationFilter(){
        return new JWTAuthenticationFilter(getUserDetailsService(),jwtTokenHelper);
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

                
        http.addFilterBefore(getJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        

        

        return http.build();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource getCorsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        // configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:[5173]"));
        configuration.setAllowedOriginPatterns(Arrays.asList("http://blogappfrontendbucket.s3-website.ap-south-1.amazonaws.com","http://localhost:[5173]"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        // configuration.setMaxAge(5000l);
        

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return urlBasedCorsConfigurationSource;
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