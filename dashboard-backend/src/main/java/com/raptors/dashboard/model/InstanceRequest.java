package com.raptors.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InstanceRequest {

    private String name;

    private URI uri;

    private String login;

    private String password;
}
