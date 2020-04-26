package com.raptors.dashboard.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
public class CustomToken extends UsernamePasswordAuthenticationToken {

    private final String key;

    CustomToken(String principal,
                String key,
                Role role) {
        super(principal, null, List.of(new SimpleGrantedAuthority(role.name())));
        this.key = key;
    }

    @Override
    public String getPrincipal() {
        return (String) super.getPrincipal();
    }

    @Override
    public String getCredentials() {
        return (String) super.getCredentials();
    }

}
