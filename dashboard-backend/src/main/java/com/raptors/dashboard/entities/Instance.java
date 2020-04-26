package com.raptors.dashboard.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;

@Builder
@Getter
@Setter
public class Instance {

    private String uuid;

    private String name;

    private URI uri;

    private String login;

    private String encryptedPassword;
}
