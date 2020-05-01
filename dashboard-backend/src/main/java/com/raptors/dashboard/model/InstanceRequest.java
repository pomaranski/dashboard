package com.raptors.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InstanceRequest implements Validated {

    private String name;

    private URI uri;

    private Boolean isHttps;

    private Integer httpPort;

    private String instanceLogin;

    private String instancePassword;

    private Integer sshPort;

    private String command;

    private String hostLogin;

    private String hostPassword;

    @Override
    public void validate() {
        notBlank(name, "Invalid name format");
        notNull(uri, "Invalid uri format");
        notNull(isHttps, "Invalid http protocol format");
        notNull(httpPort, "Invalid http port format");
        notBlank(instanceLogin, "Invalid instance login format");
        notBlank(instancePassword, "Invalid instance password format");
        notNull(sshPort, "Invalid ssh port format");
        notBlank(command, "Invalid command format");
        notBlank(hostLogin, "Invalid host login format");
        notBlank(hostPassword, "Invalid host password format");
    }
}
