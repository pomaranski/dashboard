package com.raptors.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.apache.commons.lang3.Validate.notBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthUser implements Validated {

    private String login;

    private String password;

    private String key;

    @Override
    public void validate() {
        notBlank(login, "Login cannot be blank");
        notBlank(password, "Password cannot be blank");
        matchesPattern(key, "\\p{XDigit}{32}", "Invalid key format");
    }
}
