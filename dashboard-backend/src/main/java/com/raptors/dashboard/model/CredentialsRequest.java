package com.raptors.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static org.apache.commons.lang3.Validate.matchesPattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CredentialsRequest implements Validated {

    private String publicKey;

    @Override
    public void validate() {
        matchesPattern(publicKey, "\\p{XDigit}{324}", "Invalid public key format");
    }
}
