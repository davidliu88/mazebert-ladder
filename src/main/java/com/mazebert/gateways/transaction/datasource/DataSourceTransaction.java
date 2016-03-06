package com.mazebert.gateways.transaction.datasource;

import com.mazebert.gateways.transaction.Transaction;
import com.mazebert.gateways.transaction.TransactionError;
import com.mazebert.gateways.transaction.TransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceTransaction implements Transaction {
    private final TransactionManager transactionManager;
    private final ConnectionProxy connectionProxy;

    public DataSourceTransaction(TransactionManager transactionManager, DataSource dataSource) {
        this.transactionManager = transactionManager;
        try {
            connectionProxy = new ConnectionProxy(dataSource.getConnection());
            connectionProxy.setAutoCommit(false);
        } catch (SQLException e) {
            closeConnectionQuietly();
            throw new TransactionError("Failed to create transaction.", e);
        }
    }

    @Override
    public void rollback() {
        try {
            connectionProxy.rollback();
        } catch (SQLException e) {
            throw new TransactionError("Failed to rollback transaction.", e);
        } finally {
            closeTransaction();
        }
    }

    @Override
    public void commit() {
        try {
            connectionProxy.commit();
            closeTransaction();
        } catch (SQLException e) {
            throw new TransactionError("Failed to commit transaction.", e);
        }
    }

    private void closeTransaction() {
        transactionManager.setCurrent(null);
        closeConnectionQuietly();
    }

    private void closeConnectionQuietly() {
        try {
            connectionProxy.getConnection().close();
        } catch (SQLException e) {
            // Connection is closed quietly.
        }
    }

    public Connection getConnection() {
        return connectionProxy;
    }
}
