package com.raptors.dashboard.entities;

import com.raptors.dashboard.security.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@Document
public class User {

    @Id
    private String id;

    private String login;

    private String hashedPassword;

    private Role role;

    private List<Instance> instances;

    public void addInstance(Instance instance) {
        instances.add(instance);
    }

    public void removeInstance(UUID uuid) {
        instances = instances.stream()
                .filter(instance -> !instance.getUuid().equals(uuid))
                .collect(Collectors.toList());
    }
}