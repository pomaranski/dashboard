package com.raptors.dashboard.services;

import com.raptors.dashboard.crytpo.CryptoProvider;
import com.raptors.dashboard.entities.Instance;
import com.raptors.dashboard.entities.User;
import com.raptors.dashboard.model.ExecuteCommandRequest;
import com.raptors.dashboard.model.ExecutionResult;
import com.raptors.dashboard.model.Validated;
import com.raptors.dashboard.ssh.InstanceSshConnector;
import com.raptors.dashboard.store.UserStorage;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

import static io.vavr.control.Option.ofOptional;

@Slf4j
@Service
public class SshService {

    private final UserStorage userStorage;
    private final CryptoProvider cryptoProvider;
    private final InstanceSshConnector instanceSshConnector;

    public SshService(UserStorage userStorage,
            CryptoProvider cryptoProvider,
            InstanceSshConnector instanceSshConnector) {
        this.userStorage = userStorage;
        this.cryptoProvider = cryptoProvider;
        this.instanceSshConnector = instanceSshConnector;
    }

    public ResponseEntity executeCommand(String login, String key, String instanceUuid, ExecuteCommandRequest executeCommandRequest) {
        return validate(executeCommandRequest)
                .flatMap(request -> ofOptional(userStorage.findByLogin(login))
                        .peek(user -> log.info("Retrieved for login: {}, data: {}", login, user))
                        .toEither(ResponseEntity.notFound().build()))
                .flatMap(user -> ofOptional(findInstance(instanceUuid, user))
                        .peek(instance -> log.info("Retrieved for id: {}, data: {}", instanceUuid, instance))
                        .toEither(ResponseEntity.notFound().build()))
                .fold(Function.identity(), instance -> sendSshCommand(key, instance, executeCommandRequest));
    }

    private Optional<Instance> findInstance(String instanceUuid, User user) {
        return user.getInstances().stream()
                .filter(i -> i.getUuid().equalsIgnoreCase(instanceUuid))
                .findFirst();
    }

    private Either<ResponseEntity, Validated> validate(Validated validated) {
        try {
            validated.validate();
        } catch (RuntimeException e) {
            log.info("Validation error -> {}", e.getMessage());
            return Either.left(ResponseEntity.badRequest().body(e.getMessage()));
        }
        return Either.right(validated);
    }

    private ResponseEntity sendSshCommand(String key, Instance instance, ExecuteCommandRequest executeCommandRequest) {
        log.info("Sending command through ssh");
        String plainPassword = this.cryptoProvider.decryptAes(key, instance.getEncryptedHostPassword());
        Either<ExecutionResult, Integer> executionResult = instanceSshConnector.executeScript(
                URI.create(instance.getSshUri()),
                instance.getHostLogin(),
                plainPassword,
                executeCommandRequest.getCommand());
        return executionResult.fold(ExecutionResult::toResponse, i -> ResponseEntity.ok().build());
    }
}
