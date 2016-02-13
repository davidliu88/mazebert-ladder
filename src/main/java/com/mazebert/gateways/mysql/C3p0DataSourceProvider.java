package com.mazebert.gateways.mysql;

import com.google.inject.Provider;
import com.mazebert.error.InternalServerError;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.flywaydb.core.Flyway;

import javax.inject.Inject;
import javax.sql.DataSource;

public class C3p0DataSourceProvider implements DataSourceProvider, Provider<DataSource> {
    private final String user;
    private final String password;
    private final String url;

    private ComboPooledDataSource dataSource;

    @Inject
    public C3p0DataSourceProvider(String user, String password, String url) {
        this.user = user;
        this.password = password;
        this.url = url;
    }

    @Override
    public DataSource get() {
        return getDataSource();
    }

    public synchronized DataSource getDataSource() {
        if (dataSource == null) {
            createDataSource();
            prepareDatabase();
        }

        return dataSource;
    }

    private void createDataSource() {
        try {
            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://" + url);
            dataSource.setUser(user);
            dataSource.setPassword(password);
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
