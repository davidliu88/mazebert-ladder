package com.mazebert.gateways.transaction.mocks;

import com.mazebert.gateways.transaction.TransactionError;
import com.mazebert.gateways.transaction.TransactionRunner;

public class TransactionRunnerMock implements TransactionRunner {
    private Runnable current;

    @Override
    public void runAsTransaction(Runnable runnable) {
        if (current != null) {
            throw new TransactionError("Nested transactions are not supported!");
        }

        current = runnable;
        runnable.run();
        current = null;
    }
}
