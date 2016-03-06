package com.mazebert.gateways.mysql.connection;

import com.google.inject.Provider;
import com.mazebert.error.InternalServerError;
import com.mazebert.gateways.transaction.TransactionManager;
import com.mazebert.gateways.transaction.datasource.DataSourceProxy;
import com.mazebert.gateways.transaction.datasource.DataSourceTransactionManager;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.flywaydb.core.Flyway;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

@Singleton
public class C3p0DataSourceProvider implements DataSourceProvider, Provider<DataSource> {
    private final Credentials credentials;
    private final DataSourceTransactionManager transactionManager;
    private ComboPooledDataSource dataSource;
    private DataSourceProxy dataSourceProxy;

    @Inject
    public C3p0DataSourceProvider(Credentials credentials, DataSourceTransactionManager transactionManager) {
        this.credentials = credentials;
        this.transactionManager = transactionManager;
    }

    @Override
    public DataSource get() {
        return dataSourceProxy;
    }

    @Override
    public void prepare() {
        createDataSource();
        prepareDatabase();
    }

    @Override
    public void dispose() {
        dataSource.close();
    }

    private void createDataSource() {
        try {
            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://" + credentials.getUrl());
            dataSource.setUser(credentials.getUser());
            dataSource.setPassword(credentials.getPassword());
            dataSource.setMinPoolSize(3);
            dataSource.setMaxPoolSize(30);
            dataSource.setAcquireIncrement(1);

            transactionManager.setDataSource(dataSource);
            dataSourceProxy = new DataSourceProxy(dataSource, transactionManager);
        } catch (Throwable e) {
            throw new InternalServerError("Database connection pool could not be initialized", e);
        }
    }

    private void prepareDatabase() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }
}
