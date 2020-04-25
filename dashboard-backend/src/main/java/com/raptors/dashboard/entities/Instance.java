package com.raptors.dashboard.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Instance {

    private final UUID uuid = UUID.randomUUID();

    private String name;

    private URI uri;

    private String login;

    private String encryptedPassword;
}
