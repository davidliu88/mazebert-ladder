package com.mazebert.error;

public class Unauthorized extends Error {
    public Unauthorized(String message) {
        super(message);
    }

    public Unauthorized(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
