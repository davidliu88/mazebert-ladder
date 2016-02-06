package com.mazebert.gateways.mysql;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class TestErrorDataSourceProvider implements DataSourceProvider {
    public static TestErrorDataSourceProvider instance = new TestErrorDataSourceProvider();

    @Override
    public DataSource getDataSource() {
        return new DataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                throw new SQLException();
            }

            @Override
            public Connection getConnection(String username, String password) throws SQLException {
                throw new SQLException();
            }

            @Override
            public PrintWriter getLogWriter() throws SQLException {
                throw new SQLException();
            }

            @Override
            public void setLogWriter(PrintWriter out) throws SQLException {
                throw new SQLException();
            }

            @Override
            public void setLoginTimeout(int seconds) throws SQLException {
                throw new SQLException();
            }

            @Override
            public int getLoginTimeout() throws SQLException {
                throw new SQLException();
            }

            @Override
            public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                return null;
            }

            @Override
            public <T> T unwrap(Class<T> iface) throws SQLException {
                throw new SQLException();
            }

            @Override
            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                throw new SQLException();
            }
        };
    }
}
