package com.mazebert.gateways.error;

import com.mazebert.gateways.mysql.MySqlErrorCode;
import org.jusecase.transaction.simple.TransactionExecutionError;

import java.sql.SQLException;

public class GatewayError extends RuntimeException implements TransactionExecutionError {
    public GatewayError() {
    }

    public GatewayError(String message) {
        super(message);
    }

    public GatewayError(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public boolean shouldRetryTransaction() {
        return (getCause() != null
                && getCause() instanceof SQLException
                && ((SQLException) getCause()).getErrorCode() == MySqlErrorCode.COULD_NOT_GET_LOCK);
    }
}
