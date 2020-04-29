package com.raptors.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static org.apache.commons.lang3.Validate.notBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExecuteCommandRequest implements Validated {

    private String command;

    @Override
    public void validate() {
        notBlank(command, "Command cannot be null");
    }
}
