package com.raptors.dashboard.services;

import com.raptors.dashboard.clients.RaptorsClient;
import com.raptors.dashboard.entities.Instance;
import com.raptors.dashboard.entities.User;
import com.raptors.dashboard.model.InstanceRequest;
import com.raptors.dashboard.model.InstanceResponse;
import com.raptors.dashboard.repositories.UserRepository;
import com.raptors.dashboard.security.SecurityPropertyHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecurityPropertyHolder securityPropertyHolder;
    private final RaptorsClient raptorsClient;

    public UserService(UserRepository userRepository,
                       SecurityPropertyHolder securityPropertyHolder,
                       RaptorsClient raptorsClient) {
        this.userRepository = userRepository;
        this.securityPropertyHolder = securityPropertyHolder;
        this.raptorsClient = raptorsClient;
    }

    public User getUserByLoginOrThrowException(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public ResponseEntity addInstance(String login, String encryptedKey, InstanceRequest instanceRequest) {
        try {
            instanceRequest.validate();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return userRepository.findByLogin(login)
                .map(user -> {
                    user.addInstance(mapInstanceRequestToInstance(encryptedKey, instanceRequest));
                    userRepository.save(user);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity removeInstance(String login, String uuid) {
        return userRepository.findByLogin(login)
                .map(user -> {
                    user.removeInstance(uuid);
                    userRepository.save(user);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity getInstances(String login) {
        return userRepository.findByLogin(login)
                .map(user -> ResponseEntity.ok(mapUserInstancesToInstancesResponse(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity loginToInstance(String login, String encryptedKey, String uuid) {
        return userRepository.findByLogin(login)
                .map(user -> getInstance(uuid, user)
                        .map(instance -> {
                            String plainPassword = decryptPassword(encryptedKey, instance.getEncryptedPassword());
                            return raptorsClient.authenticate(instance.getUri(), instance.getLogin(), plainPassword);
                        }).orElseGet(() -> ResponseEntity.notFound().build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
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
