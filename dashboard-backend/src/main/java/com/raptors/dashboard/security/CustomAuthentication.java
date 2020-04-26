package com.raptors.dashboard.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class CustomAuthentication extends UsernamePasswordAuthenticationToken {

    private final String key;

    private final Role role;

    CustomAuthentication(String principal,
                         String credentials,
                         String key) {
        this(principal, credentials, null, key);
    }

    CustomAuthentication(String principal,
                         Role role,
                         String key) {
        this(principal, null, role, key);
    }

    private CustomAuthentication(String principal,
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
