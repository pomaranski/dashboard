package com.raptors.dashboard.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ConnectionProblemResult implements ExecutionResult {
    @Override
    public ResponseEntity toResponse() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Couldn't connect through ssh");
    }
}
