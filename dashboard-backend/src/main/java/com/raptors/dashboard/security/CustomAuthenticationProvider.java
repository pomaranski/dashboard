package com.raptors.dashboard.security;

import com.raptors.dashboard.entities.User;
import com.raptors.dashboard.services.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserService userService) {
        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthentication customAuthentication = (CustomAuthentication) authentication;

        User user = userService.getUserByLoginOrThrowException(customAuthentication.getName());

        if (!passwordEncoder.matches(customAuthentication.getCredentials(), user.getHashedPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        if (!passwordEncoder.matches(customAuthentication.getKey(), user.getHashedKey())) {
            throw new BadCredentialsException("Invalid key");
        }

        return new CustomAuthentication(user.getLogin(), null, user.getRole(), customAuthentication.getKey());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(CustomAuthentication.class);
    }
}
