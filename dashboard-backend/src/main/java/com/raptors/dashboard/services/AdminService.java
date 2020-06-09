package com.raptors.dashboard.services;

import com.raptors.dashboard.entities.Instance;
import com.raptors.dashboard.entities.User;
import com.raptors.dashboard.model.RegisterRequest;
import com.raptors.dashboard.store.AddressStorage;
import com.raptors.dashboard.store.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.raptors.dashboard.security.Role.ROLE_OWNER;

@Slf4j
@Service
public class AdminService {

    private final UserStorage userStorage;
    private final AddressStorage addressStorage;

    public AdminService(UserStorage userStorage,
                        AddressStorage addressStorage) {
        this.userStorage = userStorage;
        this.addressStorage = addressStorage;
    }

    public ResponseEntity getAllUnAssignedUri(String login) {
        return userStorage.findByLogin(login)
                .map(user -> {
                    Set<String> uris = addressStorage.fetchAllAddress();
                    uris.removeAll(user.getInstances()
                            .stream()
                            .map(Instance::getHttpUri)
                            .collect(Collectors.toSet()));
                    return ResponseEntity.ok(uris);
                }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity register(RegisterRequest registerRequest) {
        try {
            registerRequest.validate();
        } catch (RuntimeException e) {
            log.info("Validation error -> {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return userStorage.findByLogin(registerRequest.getLogin())
                .map(user -> {
                    log.info("User already exists {}", registerRequest.getLogin());
                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body("User already exists");
                })
                .orElseGet(() -> {
                    log.info("Registered new owner {}", registerRequest.getLogin());
                    userStorage.storeUser(User.builder()
                            .login(registerRequest.getLogin())
                            .hashedPassword(registerRequest.getHashedPassword())
                            .hashedKey(registerRequest.getHashedKey())
                            .role(ROLE_OWNER)
                            .instances(List.of())
                            .build());
                    return ResponseEntity.ok().build();
                });
    }
}
