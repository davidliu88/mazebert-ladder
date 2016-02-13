package com.mazebert.error;

public abstract class Error extends RuntimeException {
    public Error(String message) {
        super(message);
    }

    public Error(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Error error = (Error) o;

        return getMessage() != null ? getMessage().equals(error.getMessage()) : error.getMessage() == null;

    }

    @Override
    public int hashCode() {
        return getMessage() != null ? getMessage().hashCode() : 0;
    }
}
