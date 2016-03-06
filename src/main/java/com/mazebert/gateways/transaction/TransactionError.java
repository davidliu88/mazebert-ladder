package com.mazebert.gateways.transaction;

import com.mazebert.gateways.error.GatewayError;

public class TransactionError extends GatewayError {
    public TransactionError(String message) {
        super(message);
    }

    public TransactionError(String message, Throwable cause) {
        super(message, cause);
    }
}
