package com.mazebert.error;

public class BadRequest extends Error {
    public BadRequest(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
