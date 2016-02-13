package com.mazebert.error;

public class Unauthorized extends Error {
    public Unauthorized(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
