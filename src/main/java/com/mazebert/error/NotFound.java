package com.mazebert.error;

public class NotFound extends Error {
    public NotFound(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
