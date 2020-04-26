package com.raptors.dashboard.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raptors.dashboard.model.AuthUser;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.raptors.dashboard.crytpo.CryptoModule.encryptAes;
import static com.raptors.dashboard.crytpo.HexUtils.bytesToHex;
import static com.raptors.dashboard.crytpo.HexUtils.hexToBytes;
import static com.raptors.dashboard.security.SecurityConstants.ENCRYPTED_KEY;
import static com.raptors.dashboard.security.SecurityConstants.ROLE;
import static com.raptors.dashboard.security.SecurityConstants.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final SecurityPropertyHolder securityPropertyHolder;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   SecurityPropertyHolder securityPropertyHolder) {
        this.authenticationManager = authenticationManager;
        this.securityPropertyHolder = securityPropertyHolder;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            AuthUser authUser = new ObjectMapper().readValue(req.getInputStream(), AuthUser.class);
            authUser.validate();

            CustomAuthentication authentication = new CustomAuthentication(
                    authUser.getLogin(),
                    authUser.getPassword(),
                    authUser.getKey());

            return authenticationManager.authenticate(authentication);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @SneakyThrows
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {
        CustomAuthentication customAuthentication = (CustomAuthentication) auth;
        String encryptedKey = bytesToHex(encryptAes(
                hexToBytes(securityPropertyHolder.getKek()),
                hexToBytes(customAuthentication.getKey())));

        String token = JWT.create()
                .withSubject(customAuthentication.getName())
                .withClaim(ENCRYPTED_KEY, encryptedKey)
                .withClaim(ROLE, customAuthentication.getRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + securityPropertyHolder.getTokenExpiration()))
                .sign(HMAC512(hexToBytes(securityPropertyHolder.getTokenSecret())));
        res.addHeader(AUTHORIZATION, TOKEN_PREFIX + token);
    }
}
