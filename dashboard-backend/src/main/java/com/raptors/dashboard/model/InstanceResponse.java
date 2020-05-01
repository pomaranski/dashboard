package com.raptors.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class InstanceResponse {

    private String uuid;

    private String name;

    private String hostUri;

    private Boolean isHttps;

    private Integer httpPort;

    private String instanceLogin;

    private Integer sshPort;

    private String command;

    private String hostLogin;
}
