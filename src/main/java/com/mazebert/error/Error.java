package com.mazebert.error;

public class Error extends RuntimeException {
    private final Type type;

    public Error(Type type) {
        this(type, null, null);
    }

    public Error(Type type, String message) {
        this(type, message, null);
    }

    public Error(Type type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Error error = (Error) o;

        if (type != error.type) return false;
        return getMessage() != null ? getMessage().equals(error.getMessage()) : error.getMessage() == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        return result;
    }
}
