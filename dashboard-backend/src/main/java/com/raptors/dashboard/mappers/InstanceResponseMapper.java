package com.raptors.dashboard.mappers;

import com.raptors.dashboard.entities.Instance;
import com.raptors.dashboard.model.InstanceResponse;

import java.util.List;
import java.util.stream.Collectors;

public class InstanceResponseMapper {

    private InstanceResponseMapper() {

    }

    public static List<InstanceResponse> mapFromInstances(List<Instance> instances) {
        return instances.stream()
                .map(InstanceResponseMapper::mapFromInstance)
                .collect(Collectors.toList());
    }

    public static InstanceResponse mapFromInstance(Instance instance) {
        return InstanceResponse.builder()
                .uuid(instance.getUuid())
                .name(instance.getName())
                .httpUri(instance.getHttpUri())
                .instanceLogin(instance.getInstanceLogin())
                .sshUri(instance.getSshUri())
                .hostLogin(instance.getHostLogin())
                .build();
    }
}
