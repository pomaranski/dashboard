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

    private UUID uuid;

    private String name;

    private URI uri;

    private String login;

    private String encryptedPassword;
}
