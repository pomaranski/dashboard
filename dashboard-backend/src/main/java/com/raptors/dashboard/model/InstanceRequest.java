package com.raptors.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InstanceRequest implements Validated {

    private static final String HTTP_SCHEME_REGEX = "^(http|https)";
    private static final String SSH_SCHEME_REGEX = "ssh";

    private String name;

    private URI httpUri;

    private String instanceLogin;

    private String instancePassword;

    private URI sshUri;

    private String command;

    private String hostLogin;

    private String hostPassword;

    @Override
    public void validate() {
        notBlank(name, "Name cannot be blank");
        notNull(httpUri, "Http uri is null");
        isTrue(httpUri.getScheme().matches(HTTP_SCHEME_REGEX), "Http uri has not http scheme");
        notBlank(httpUri.getHost(), "Http host is blank");
        notBlank(instanceLogin, "Instance login cannot be blank");
        notBlank(instancePassword, "Instance password cannot be blank");
        notNull(sshUri, "Ssh uri is null");
        isTrue(sshUri.getScheme().matches(SSH_SCHEME_REGEX), "Ssh uri has not ssh scheme");
        notBlank(sshUri.getHost(), "Ssh host is blank");
        notBlank(command, "Command cannot be blank");
        notBlank(hostLogin, "Host login cannot be blank");
        notBlank(hostPassword, "Host password cannot be blank");
    }
}
