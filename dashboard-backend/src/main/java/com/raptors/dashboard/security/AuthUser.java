package com.raptors.dashboard.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class AuthUser {

    private String login;

    private String password;

    private String key;
}
