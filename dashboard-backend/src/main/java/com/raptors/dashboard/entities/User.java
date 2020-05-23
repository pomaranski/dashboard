package com.raptors.dashboard.entities;

import com.raptors.dashboard.security.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@Document
@ToString
public class User {

    @Id
    private String id;

    private String login;

    private String hashedPassword;

    private String hashedKey;

    private Role role;

    private List<Instance> instances;

    public void addInstance(Instance instance) {
        if (instances == null) {
            instances = new ArrayList<>();
        }
        instances.add(instance);
    }

    public boolean removeInstance(String uuid) {
        if (instances != null) {
            return instances.stream()
                    .filter(instance -> instance.getUuid().equals(uuid))
                    .findFirst()
                    .map(instance -> instances.remove(instance))
                    .orElse(false);
        }

        return false;
    }
}
