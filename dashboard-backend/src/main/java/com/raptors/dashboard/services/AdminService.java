package com.raptors.dashboard.services;

import com.raptors.dashboard.entities.Instance;
import com.raptors.dashboard.store.AddressStorage;
import com.raptors.dashboard.store.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

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
                    Set<URI> uris = addressStorage.fetchAllAddress();
                    uris.removeAll(user.getInstances()
                            .stream()
                            .map(Instance::getUri)
                            .collect(Collectors.toSet()));
                    return ResponseEntity.ok(uris);
                }).orElse(ResponseEntity.notFound().build());
    }
}
