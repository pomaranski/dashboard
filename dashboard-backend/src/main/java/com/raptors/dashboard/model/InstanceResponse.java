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

    private String httpUri;

    private String instanceLogin;

    private String sshUri;

    private String command;

    private String hostLogin;
}
