package com.pranshu.blogapp.util;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public record JwtPrincipal(int userId, String name, String username, Collection<? extends GrantedAuthority> authorities) {
}
