package com.raptors.dashboard.services;

import com.raptors.dashboard.clients.RaptorsClient;
import com.raptors.dashboard.entities.Instance;
import com.raptors.dashboard.entities.User;
import com.raptors.dashboard.model.InstanceRequest;
import com.raptors.dashboard.model.InstanceResponse;
import com.raptors.dashboard.security.SecurityPropertyHolder;
import com.raptors.dashboard.store.AddressStorage;
import com.raptors.dashboard.store.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.raptors.dashboard.crytpo.CryptoModule.decryptAes;
import static com.raptors.dashboard.crytpo.CryptoModule.encryptAes;
import static com.raptors.dashboard.crytpo.HexUtils.bytesToHex;
import static com.raptors.dashboard.crytpo.HexUtils.hexToBytes;
import static java.nio.charset.StandardCharsets.US_ASCII;

@Slf4j
@Service
public class AuthorizedUserService {

    private final UserStorage userStorage;
    private final AddressStorage addressStorage;
    private final RaptorsClient raptorsClient;
    private final SecurityPropertyHolder securityPropertyHolder;

    public AuthorizedUserService(UserStorage userStorage,
                                 AddressStorage addressStorage,
                                 RaptorsClient raptorsClient,
                                 SecurityPropertyHolder securityPropertyHolder) {
        this.userStorage = userStorage;
        this.addressStorage = addressStorage;
        this.raptorsClient = raptorsClient;
        this.securityPropertyHolder = securityPropertyHolder;
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
                    Instance instance = mapInstanceRequestToInstance(encryptedKey, instanceRequest);
                    addressStorage.storeAddress(instance.getUri());
                    user.addInstance(instance);
                    userStorage.storeUser(user);
                    return ResponseEntity.ok(mapInstanceToInstanceResponse(instance));
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
                .map(user -> ResponseEntity.ok(mapUserInstancesToInstancesResponse(user)))
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
        String plainPassword = decryptPassword(encryptedKey, instance.getEncryptedPassword());
        return raptorsClient.authenticate(instance.getUri(), instance.getLogin(), plainPassword);
    }

    private Optional<Instance> getInstance(String uuid, User user) {
        return user.getInstances().stream()
                .filter(instance -> instance.getUuid().equals(uuid))
                .findFirst();
    }

    private Instance mapInstanceRequestToInstance(String encryptedKey, InstanceRequest instanceRequest) {
        return Instance.builder()
                .uuid(UUID.randomUUID().toString())
                .name(instanceRequest.getName())
                .login(instanceRequest.getLogin())
                .uri(instanceRequest.getUri())
                .encryptedPassword(encryptPassword(encryptedKey, instanceRequest))
                .build();
    }

    private String encryptPassword(String encryptedKey, InstanceRequest instanceRequest) {
        byte[] plainKey = decryptAes(hexToBytes(securityPropertyHolder.getKek()), hexToBytes(encryptedKey));
        return bytesToHex(encryptAes(plainKey, instanceRequest.getPassword().getBytes(US_ASCII)));
    }

    private String decryptPassword(String encryptedKey, String encryptedPassword) {
        byte[] plainKey = decryptAes(hexToBytes(securityPropertyHolder.getKek()), hexToBytes(encryptedKey));
        return new String(decryptAes(plainKey, hexToBytes(encryptedPassword)), US_ASCII);
    }

    private List<InstanceResponse> mapUserInstancesToInstancesResponse(User user) {
        return user.getInstances().stream()
                .map(this::mapInstanceToInstanceResponse)
                .collect(Collectors.toList());
    }

    private InstanceResponse mapInstanceToInstanceResponse(Instance instance) {
        return InstanceResponse.builder()
                .uuid(instance.getUuid())
                .name(instance.getName())
                .uri(instance.getUri())
                .build();
    }
}
