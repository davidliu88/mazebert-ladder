package com.mazebert.gateways.error;

import com.mazebert.gateways.mysql.MySqlErrorCode;

import java.sql.SQLException;

public class GatewayError extends RuntimeException {
    private boolean isTransactionError;

    public GatewayError() {
    }

    public GatewayError(String message) {
        super(message);
    }

    public GatewayError(String message, Throwable cause) {
        super(message, cause);

        if (cause instanceof SQLException) {
            addExtraInformation((SQLException)cause);
        }
    }

    public boolean isTransactionError() {
        return isTransactionError;
    }

    protected void addExtraInformation(SQLException exception) {
        if (exception.getErrorCode() == MySqlErrorCode.COULD_NOT_GET_LOCK) {
            isTransactionError = true;
        }
    }
}
