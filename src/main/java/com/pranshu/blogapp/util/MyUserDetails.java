package com.pranshu.blogapp.util;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pranshu.blogapp.constant.Role;
import com.pranshu.blogapp.entity.User;

public class MyUserDetails implements UserDetails {
    private User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        if (roles.isEmpty()) { // not needed. just for safety.
            return Set.of(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
        }
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new) // its constructor expects String not Role
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return this.user.getName();
    }

    public int getId() {
        return this.user.getId();
    }

}

class Test {
    public static void main(String[] args) {
        SimpleGrantedAuthority s1 = new SimpleGrantedAuthority("ROLE_USER");
        System.out.println(s1);
        System.out.println(s1.getAuthority());
    }
}