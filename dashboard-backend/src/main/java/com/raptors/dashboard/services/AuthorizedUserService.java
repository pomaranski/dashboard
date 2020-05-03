package com.raptors.dashboard.services;

import com.raptors.dashboard.crytpo.CryptoProvider;
import com.raptors.dashboard.entities.Instance;
import com.raptors.dashboard.entities.User;
import com.raptors.dashboard.model.CredentialsRequest;
import com.raptors.dashboard.model.CredentialsResponse;
import com.raptors.dashboard.model.InstanceRequest;
import com.raptors.dashboard.store.AddressStorage;
import com.raptors.dashboard.store.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.raptors.dashboard.mappers.InstanceRequestMapper.mapToInstance;
import static com.raptors.dashboard.mappers.InstanceResponseMapper.mapFromInstance;
import static com.raptors.dashboard.mappers.InstanceResponseMapper.mapFromInstances;

@Slf4j
@Service
public class AuthorizedUserService {

    private final UserStorage userStorage;
    private final AddressStorage addressStorage;
    private final CryptoProvider cryptoProvider;

    public AuthorizedUserService(UserStorage userStorage,
                                 AddressStorage addressStorage,
                                 CryptoProvider cryptoProvider) {
        this.userStorage = userStorage;
        this.addressStorage = addressStorage;
        this.cryptoProvider = cryptoProvider;
    }

    public ResponseEntity addInstance(String login, String encryptedKey, InstanceRequest instanceRequest) {
        try {
            instanceRequest.validate();
        } catch (RuntimeException e) {
            log.info("Validation error -> {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return userStorage.findByLogin(login)
                .map(user -> {
                    log.info("Add instance {} for user {}", instanceRequest.getName(), login);
                    Instance instance = mapToInstance(instanceRequest, encryptedKey, cryptoProvider);
                    addressStorage.storeAddress(instance.getHttpUri());
                    user.addInstance(instance);
                    userStorage.storeUser(user);
                    return ResponseEntity.ok(mapFromInstance(instance));
                }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity removeInstance(String login, String uuid) {
        return userStorage.findByLogin(login)
                .map(user -> {
                    log.info("Remove instance {} from user {}", uuid, login);
                    user.removeInstance(uuid);
                    userStorage.storeUser(user);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity getInstances(String login) {
        return userStorage.findByLogin(login)
                .map(user -> {
                    log.info("Get instances for user {}", login);
                    return ResponseEntity.ok(mapFromInstances(user.getInstances()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity getCredentials(String login,
                                         String encryptedKey,
                                         String uuid,
                                         CredentialsRequest credentialsRequest) {
        try {
            credentialsRequest.validate();
        } catch (RuntimeException e) {
            log.info("Validation error -> {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return userStorage.findByLogin(login)
                .map(user -> getInstance(uuid, user)
                        .map(instance -> {
                            log.info("Get credentials {} for user {}", uuid, login);
                            return getCredentials(encryptedKey, instance, credentialsRequest.getPublicKey());
                        })
                        .orElseGet(() -> ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    private ResponseEntity getCredentials(String encryptedKey, Instance instance, String publicKey) {
        String plainPassword = cryptoProvider.decryptAes(encryptedKey, instance.getEncryptedInstancePassword());
        return ResponseEntity.ok(CredentialsResponse.builder()
                .login(instance.getInstanceLogin())
                .encryptedPassword(cryptoProvider.encryptRsa(publicKey, plainPassword))
                .build());
    }

    private Optional<Instance> getInstance(String uuid, User user) {
        return user.getInstances().stream()
                .filter(instance -> instance.getUuid().equals(uuid))
                .findFirst();
    }
}
