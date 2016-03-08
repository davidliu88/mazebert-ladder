package com.mazebert.gateways.transaction.datasource;

import com.mazebert.gateways.error.GatewayError;
import com.mazebert.gateways.transaction.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

@Singleton
public class DataSourceTransactionManager implements TransactionManager {
    private DataSource dataSource;
    private final ThreadLocal<Transaction> transactions;
    private int maxTransactionAttempts = 5;

    @Inject
    public DataSourceTransactionManager() {
        transactions = new ThreadLocal<>();
    }

    @Override
    public void runAsTransaction(final Runnable task) {
        runAsTransaction((TransactionTask<Void>) () -> {
            task.run();
            return null;
        });
    }

    @Override
    public <Value> Value runAsTransaction(final TransactionTask<Value> task) {
        return runAsTransaction(task, maxTransactionAttempts - 1);
    }

    private <Value> Value runAsTransaction(final TransactionTask<Value> task, int attemptsLeft) {
        Transaction transaction = startTransaction();
        try {
            Value result = task.run();
            transaction.commit();
            return result;
        } catch (GatewayError gatewayError){
            if (gatewayError.isTransactionError() && attemptsLeft > 0) {
                transaction.rollback();
                return runAsTransaction(task, attemptsLeft - 1);
            } else {
                transaction.rollback();
                throw gatewayError;
            }
        } catch (Throwable throwable) {
            transaction.rollback();
            throw throwable;
        }
    }

    @Override
    public Transaction startTransaction() {
        if (getCurrent() != null) {
            throw new TransactionError("Nested transactions are not supported!");
        }

        Transaction transaction = new DataSourceTransaction(this, dataSource);
        setCurrent(transaction);
        return transaction;
    }

    @Override
    public Transaction getCurrent() {
        return transactions.get();
    }

    @Override
    public void setCurrent(Transaction transaction) {
        transactions.set(transaction);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int getMaxTransactionAttempts() {
        return maxTransactionAttempts;
    }

    public void setMaxTransactionAttempts(int maxTransactionAttempts) {
        this.maxTransactionAttempts = maxTransactionAttempts;
    }
}
