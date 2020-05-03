package com.raptors.dashboard.mappers;

import com.raptors.dashboard.crytpo.CryptoProvider;
import com.raptors.dashboard.entities.Instance;
import com.raptors.dashboard.model.InstanceRequest;

import java.util.UUID;

public class InstanceRequestMapper {

    private InstanceRequestMapper() {

    }

    public static Instance mapToInstance(InstanceRequest instanceRequest,
                                         String encryptedKey,
                                         CryptoProvider cryptoProvider) {
        return Instance.builder()
                .uuid(UUID.randomUUID().toString())
                .name(instanceRequest.getName())
                .httpUri(instanceRequest.getHttpUri().toString())
                .instanceLogin(instanceRequest.getInstanceLogin())
                .encryptedInstancePassword(cryptoProvider.encryptAes(encryptedKey, instanceRequest.getInstancePassword()))
                .sshUri(instanceRequest.getSshUri().toString())
                .command(instanceRequest.getCommand())
                .hostLogin(instanceRequest.getHostLogin())
                .encryptedHostPassword(cryptoProvider.encryptAes(encryptedKey, instanceRequest.getHostPassword()))
                .build();
    }
}
