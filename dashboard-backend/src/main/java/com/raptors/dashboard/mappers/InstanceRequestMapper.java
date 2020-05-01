package com.raptors.dashboard.mappers;

import com.raptors.dashboard.crytpo.CryptoProvider;
import com.raptors.dashboard.entities.Instance;
import com.raptors.dashboard.model.HttpProtocol;
import com.raptors.dashboard.model.InstanceRequest;

import java.util.UUID;

import static com.raptors.dashboard.model.HttpProtocol.HTTP;
import static com.raptors.dashboard.model.HttpProtocol.HTTPS;

public class InstanceRequestMapper {

    private InstanceRequestMapper() {

    }

    public static Instance mapToInstance(InstanceRequest instanceRequest, String encryptedKey, CryptoProvider cryptoProvider) {
        return Instance.builder()
                .uuid(UUID.randomUUID().toString())
                .name(instanceRequest.getName())
                .hostUri(instanceRequest.getUri().getHost())
                .httpProtocol(resolveHttpProtocol(instanceRequest.getIsHttps()))
                .httpPort(instanceRequest.getHttpPort())
                .instanceLogin(instanceRequest.getInstanceLogin())
                .encryptedInstancePassword(cryptoProvider.encrypt(encryptedKey, instanceRequest.getInstancePassword()))
                .sshPort(instanceRequest.getSshPort())
                .hostLogin(instanceRequest.getHostLogin())
                .encryptedHostPassword(cryptoProvider.encrypt(encryptedKey, instanceRequest.getHostPassword()))
                .build();
    }

    private static HttpProtocol resolveHttpProtocol(boolean isHttps) {
        return isHttps ? HTTPS : HTTP;
    }
}
