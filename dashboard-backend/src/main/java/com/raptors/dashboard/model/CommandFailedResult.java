package com.raptors.dashboard.model;

import org.springframework.http.ResponseEntity;

public class CommandFailedResult implements ExecutionResult {
    @Override
    public ResponseEntity toResponse() {
        return ResponseEntity.badRequest().body("Wrong command was send");
    }
}
