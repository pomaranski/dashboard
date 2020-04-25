package com.raptors.dashboard.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

class CustomAuthentication extends UsernamePasswordAuthenticationToken {

    @Getter
    private final String key;

    @Getter
    private final Role role;

    CustomAuthentication(String principal,
                         String credentials,
                         Role role,
                         String key) {
        super(principal, credentials);
        this.role = role;
        this.key = key;
    }

    @Override
    public String getCredentials() {
        return (String) super.getCredentials();
    }
}
