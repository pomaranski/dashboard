package com.raptors.dashboard.security;

import com.raptors.dashboard.entities.User;
import com.raptors.dashboard.store.UserStorage;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserStorage userStorage;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserStorage userStorage, PasswordEncoder passwordEncoder) {
        this.userStorage = userStorage;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthentication customAuthentication = (CustomAuthentication) authentication;

        User user = userStorage.getUserByLoginOrThrowException(customAuthentication.getName());

        if (!passwordEncoder.matches(customAuthentication.getCredentials(), user.getHashedPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        if (!passwordEncoder.matches(customAuthentication.getKey(), user.getHashedKey())) {
            throw new BadCredentialsException("Invalid key");
        }

        return new CustomAuthentication(user.getLogin(), user.getRole(), customAuthentication.getKey());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(CustomAuthentication.class);
    }
}
