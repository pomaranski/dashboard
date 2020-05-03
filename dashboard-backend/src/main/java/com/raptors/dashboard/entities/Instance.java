package com.raptors.dashboard.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Instance {

    private String uuid;

    private String name;

    private String httpUri;

    private String instanceLogin;

    private String encryptedInstancePassword;

    private String sshUri;

    private String command;

    private String hostLogin;

    private String encryptedHostPassword;
}
