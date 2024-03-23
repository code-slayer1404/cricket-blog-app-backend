package com.pranshu.blogapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BlogAppApplication implements CommandLineRunner {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(bCryptPasswordEncoder.encode("Pranshu@123"));
	}

	

}
