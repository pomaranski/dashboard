package com.raptors.dashboard.entities;

import com.raptors.dashboard.model.HttpProtocol;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Instance {

    private String uuid;

    private String name;

    private String hostUri;

    private HttpProtocol httpProtocol;

    private Integer httpPort;

    private String instanceLogin;

    private String encryptedInstancePassword;

    private Integer sshPort;

    private String command;

    private String hostLogin;

    private String encryptedHostPassword;
}
