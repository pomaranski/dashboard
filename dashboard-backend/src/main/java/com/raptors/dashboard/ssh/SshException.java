package com.raptors.dashboard.ssh;

public class SshException extends RuntimeException {
    public SshException(String message) {
        super(message);
    }

    public SshException(String message, Throwable cause) {
        super(message, cause);
    }
}
