package com.raptors.dashboard.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class AuthUser {

    private String login;

    private String password;

    private String key;

    void validate() {
        List<Supplier<Pair<Boolean, String>>> validators = List.of(
                () -> Pair.of(login.isBlank(), "Invalid login format"),
                () -> Pair.of(password.isBlank(), "Invalid password format"),
                () -> Pair.of(!Pattern.compile("\\p{XDigit}{32}").matcher(key).matches(), "Invalid key format"));


        for (Supplier<Pair<Boolean, String>> validator : validators) {
            if (validator.get().getFirst()) {
                throw new IllegalStateException(validator.get().getSecond());
            }
        }
    }
}
