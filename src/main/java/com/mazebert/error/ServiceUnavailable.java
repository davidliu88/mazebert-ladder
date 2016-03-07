package com.mazebert.error;

public class ServiceUnavailable extends Error {
    public ServiceUnavailable(String message) {
        super(message);
    }

    public ServiceUnavailable(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 503;
    }
}
