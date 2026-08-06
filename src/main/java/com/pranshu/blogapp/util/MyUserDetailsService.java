package com.pranshu.blogapp.util;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.repository.UserRepo;

public class MyUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow();
        if (user == null) {  //VVIP
            throw new UsernameNotFoundException(username + " not found");
        }
        UserDetails userDetails = new MyUserDetails(user);
        return userDetails;
    }

}
