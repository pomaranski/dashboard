package com.raptors.dashboard.management;

import com.raptors.dashboard.entities.User;
import com.raptors.dashboard.model.AuthUser;
import com.raptors.dashboard.security.Role;
import com.raptors.dashboard.store.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@Component
@RestControllerEndpoint(id = "register")
public class RegistrationController {

    private static final String REGISTER = "/register";
    private static final String REGISTER_OWNER = REGISTER + "/owner";
    private static final String REGISTER_ADMIN = REGISTER + "/admin";
    private final UserStorage userStorage;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserStorage userStorage, PasswordEncoder passwordEncoder) {
        this.userStorage = userStorage;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = REGISTER_ADMIN,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerAdmin(@RequestBody AuthUser authUser) {
        log.info("Received request to register admin: login={}", authUser.getLogin());

        return register(authUser, Role.ROLE_ADMIN);
    }

    @PostMapping(path = REGISTER_OWNER,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerOwner(@RequestBody AuthUser authUser) {
        log.info("Received request to register owner: login={}", authUser.getLogin());

        return register(authUser, Role.ROLE_OWNER);
    }

    private ResponseEntity register(AuthUser authUser, Role role) {
        try {
            authUser.validate();
        } catch (RuntimeException e) {
            log.info("Validation error -> {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        userStorage.storeUser(User.builder()
                .login(authUser.getLogin())
                .hashedPassword(passwordEncoder.encode(authUser.getPassword()))
                .hashedKey(passwordEncoder.encode(authUser.getKey()))
                .role(role)
                .instances(List.of())
                .build());

        log.info("Registered: login={}", authUser.getLogin());

        return ResponseEntity.ok().build();
    }
}
