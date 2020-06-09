package com.raptors.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RegisterRequest implements Validated {

    private String login;

    private String hashedPassword;

    private String hashedKey;

    @Override
    public void validate() {
        notBlank(login, "Login cannot be blank");
        notNull(hashedPassword, "Hashed password cannot be null");
        matchesPattern(hashedPassword, "^\\$2a?\\$\\d{1,2}\\$[.\\/A-Za-z0-9]{53}$", "Invalid hashed password");
        notNull(hashedKey, "Hashed key cannot be null");
        matchesPattern(hashedKey, "^\\$2a?\\$\\d{1,2}\\$[.\\/A-Za-z0-9]{53}$", "Invalid hashed key");
    }
}
