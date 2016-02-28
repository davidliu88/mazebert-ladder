package com.mazebert.gateways.mysql.connection;

import com.google.inject.Provider;
import com.mazebert.error.InternalServerError;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.flywaydb.core.Flyway;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

@Singleton
public class C3p0DataSourceProvider implements DataSourceProvider, Provider<DataSource> {
    private final Credentials credentials;
    private ComboPooledDataSource dataSource;

    @Inject
    public C3p0DataSourceProvider(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public DataSource get() {
        return dataSource;
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
        } catch (Throwable e) {
            throw new InternalServerError("Database connection pool could not be initialized", e);
        }
    }

    private void prepareDatabase() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }
}
