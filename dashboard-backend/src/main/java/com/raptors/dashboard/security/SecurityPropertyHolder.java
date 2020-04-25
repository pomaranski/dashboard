package com.raptors.dashboard.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SecurityPropertyHolder {

    private final String tokenSecret;
    private final long tokenExpiration;
    private final String kek;

    public SecurityPropertyHolder(@Value("${jwt.secret}") String tokenSecret,
                                  @Value("${jwt.expiration}") long tokenExpiration,
                                  @Value("${jwt.key.kek}") String kek) {
        this.tokenSecret = tokenSecret;
        this.tokenExpiration = tokenExpiration;
        this.kek = kek;
    }
}
