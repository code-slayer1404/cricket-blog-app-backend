package com.pranshu.blogapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.repository.UserRepo;

public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {  //VVIP
            throw new UsernameNotFoundException(username + " not found");
        }
        UserDetails userDetails = new MyUserDetails(user);
        return userDetails;
    }

}
