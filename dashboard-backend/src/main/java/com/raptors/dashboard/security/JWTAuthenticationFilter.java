package com.raptors.dashboard.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raptors.dashboard.entities.User;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.raptors.dashboard.crytpo.CryptoModule.encryptAes;
import static com.raptors.dashboard.crytpo.HexUtils.bytesToHex;
import static com.raptors.dashboard.security.SecurityConstants.ENCRYPTED_KEY;
import static com.raptors.dashboard.security.SecurityConstants.ROLE;
import static com.raptors.dashboard.security.SecurityConstants.TOKEN_PREFIX;
import static java.nio.charset.StandardCharsets.US_ASCII;
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

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    authUser.getLogin(),
                    authUser.getPassword(),
                    new ArrayList<>());

            authentication.setDetails(authUser.getKey().getBytes(US_ASCII));

            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {
        CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
        User user = customUserDetails.getUser();
        String encryptedKey = bytesToHex(encryptAes(
                securityPropertyHolder.getKek().getBytes(US_ASCII),
                (byte[]) auth.getDetails()));
        Role role = user.getRole();

        String token = JWT.create()
                .withSubject(user.getLogin())
                .withClaim(ENCRYPTED_KEY, encryptedKey)
                .withClaim(ROLE, role.name())
                .withExpiresAt(new Date(System.currentTimeMillis() + securityPropertyHolder.getTokenExpiration()))
                .sign(HMAC512(securityPropertyHolder.getTokenSecret().getBytes(US_ASCII)));
        res.addHeader(AUTHORIZATION, TOKEN_PREFIX + token);
    }
}
