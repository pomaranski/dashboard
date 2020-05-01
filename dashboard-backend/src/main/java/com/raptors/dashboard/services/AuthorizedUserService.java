package com.raptors.dashboard.services;

import com.raptors.dashboard.clients.RaptorsClient;
import com.raptors.dashboard.crytpo.CryptoProvider;
import com.raptors.dashboard.entities.Instance;
import com.raptors.dashboard.entities.User;
import com.raptors.dashboard.model.InstanceRequest;
import com.raptors.dashboard.store.AddressStorage;
import com.raptors.dashboard.store.UserStorage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;

import static com.raptors.dashboard.mappers.InstanceRequestMapper.mapToInstance;
import static com.raptors.dashboard.mappers.InstanceResponseMapper.mapFromInstance;
import static com.raptors.dashboard.mappers.InstanceResponseMapper.mapFromInstances;

@Slf4j
@Service
public class AuthorizedUserService {

    private final UserStorage userStorage;
    private final AddressStorage addressStorage;
    private final RaptorsClient raptorsClient;
    private final CryptoProvider cryptoProvider;

    public AuthorizedUserService(UserStorage userStorage,
                                 AddressStorage addressStorage,
                                 RaptorsClient raptorsClient,
                                 CryptoProvider cryptoProvider) {
        this.userStorage = userStorage;
        this.addressStorage = addressStorage;
        this.raptorsClient = raptorsClient;
        this.cryptoProvider = cryptoProvider;
    }

    public ResponseEntity addInstance(String login, String encryptedKey, InstanceRequest instanceRequest) {
        try {
            instanceRequest.validate();
        } catch (RuntimeException e) {
            log.info("Validate error {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return userStorage.findByLogin(login)
                .map(user -> {
                    log.info("Add instance {} to user {}", instanceRequest.getName(), login);
                    Instance instance = mapToInstance(instanceRequest, encryptedKey, cryptoProvider);
                    addressStorage.storeAddress(instance.getHostUri());
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
                .map(user -> ResponseEntity.ok(mapFromInstances(user.getInstances())))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity loginToInstance(String login, String encryptedKey, String uuid) {
        return userStorage.findByLogin(login)
                .map(user -> getInstance(uuid, user)
                        .map(instance -> authenticate(encryptedKey, instance))
                        .orElseGet(() -> ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    private ResponseEntity authenticate(String encryptedKey, Instance instance) {
        String plainPassword = cryptoProvider.decrypt(encryptedKey, instance.getEncryptedInstancePassword());
        return raptorsClient.authenticate(resolveUri(instance), instance.getInstanceLogin(), plainPassword);
    }

    @SneakyThrows
    private URI resolveUri(Instance instance) {
        return new URIBuilder()
                .setScheme(instance.getHttpProtocol().name())
                .setHost(instance.getHostUri())
                .setPort(instance.getHttpPort())
                .build();
    }

    private Optional<Instance> getInstance(String uuid, User user) {
        return user.getInstances().stream()
                .filter(instance -> instance.getUuid().equals(uuid))
                .findFirst();
    }
}
