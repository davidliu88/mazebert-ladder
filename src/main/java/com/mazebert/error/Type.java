package com.mazebert.error;

public enum Type {
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    Type(int statusCode) {
        this.statusCode = statusCode;
    }

    private final int statusCode;

    public int getStatusCode() {
        return statusCode;
    }
}
