package com.pranshu.blogapp;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class TestConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoderBean(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapperBean(){
        return new ModelMapper();
    }
    
}
