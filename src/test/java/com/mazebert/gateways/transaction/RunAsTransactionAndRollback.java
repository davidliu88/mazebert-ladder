package com.mazebert.gateways.transaction;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RunAsTransactionAndRollback implements TestRule {
    private final TransactionRunner transactionRunner;

    public RunAsTransactionAndRollback(TransactionRunner transactionRunner) {
        this.transactionRunner = transactionRunner;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            private Throwable statementException;

            @Override
            public void evaluate() throws Throwable {
                try {
                    transactionRunner.runAsTransaction((Runnable) () -> {
                        try {
                            statement.evaluate();
                        } catch (Throwable throwable) {
                            if (throwable instanceof RuntimeException) {
                                throw (RuntimeException)throwable;
                            } else {
                                statementException = throwable;
                            }
                        }

                        throw new RollbackException();
                    });
                } catch (RollbackException e) {
                    // Expected exception to cause rollback.
                }

                if (statementException != null) {
                    throw statementException;
                }
            }
        };
    }

    private static class RollbackException extends RuntimeException {
    }
}
