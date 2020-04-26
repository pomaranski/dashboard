package com.raptors.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class InstanceResponse {

    private String name;

    private URI uri;
}
