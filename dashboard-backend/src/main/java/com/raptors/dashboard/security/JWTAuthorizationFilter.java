package com.raptors.dashboard.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ch.qos.logback.core.CoreConstants.EMPTY_STRING;
import static com.raptors.dashboard.crytpo.HexUtils.hexToBytes;
import static com.raptors.dashboard.security.SecurityConstants.ENCRYPTED_KEY;
import static com.raptors.dashboard.security.SecurityConstants.ROLE;
import static com.raptors.dashboard.security.SecurityConstants.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecurityPropertyHolder securityPropertyHolder;

    public JWTAuthorizationFilter(AuthenticationManager authManager,
                                  SecurityPropertyHolder securityPropertyHolder) {
        super(authManager);
        this.securityPropertyHolder = securityPropertyHolder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(AUTHORIZATION);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        CustomToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private CustomToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            DecodedJWT decodedJwt = JWT.require(
                    Algorithm.HMAC512(hexToBytes(securityPropertyHolder.getTokenSecret())))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, EMPTY_STRING));

            String encryptedKey = decodedJwt.getClaim(ENCRYPTED_KEY).asString();
            String login = decodedJwt.getSubject();
            Role role = Role.valueOf(decodedJwt.getClaim(ROLE).asString());

            return new CustomToken(login, encryptedKey, role);
        }
        return null;
    }
}
