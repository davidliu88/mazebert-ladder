package com.mazebert.gateways.error;

public class GatewayError extends RuntimeException {
    public GatewayError() {
    }

    public GatewayError(String message) {
        super(message);
    }

    public GatewayError(String message, Throwable cause) {
        super(message, cause);
    }
}
