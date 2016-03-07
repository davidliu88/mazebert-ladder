package com.mazebert.gateways.transaction.datasource;

import com.mazebert.gateways.transaction.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

@Singleton
public class DataSourceTransactionManager implements TransactionManager {
    private DataSource dataSource;
    private final ThreadLocal<Transaction> transactions;

    @Inject
    public DataSourceTransactionManager() {
        transactions = new ThreadLocal<>();
    }

    @Override
    public void runAsTransaction(Runnable task) {
        Transaction transaction = startTransaction();
        try {
            task.run();
            transaction.commit();
        } catch (Throwable throwable) {
            transaction.rollback();
            throw throwable;
        }
    }

    @Override
    public <Value> Value runAsTransaction(TransactionTask<Value> task) {
        Transaction transaction = startTransaction();
        try {
            Value result = task.run();
            transaction.commit();
            return result;
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
}
