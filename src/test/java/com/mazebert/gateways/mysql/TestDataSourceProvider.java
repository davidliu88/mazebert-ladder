package com.mazebert.gateways.mysql;

import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

public class TestDataSourceProvider extends C3p0DataSourceProvider {
    public static final TestDataSourceProvider instance = new TestDataSourceProvider();

    private TestDataSourceProvider() {
        super("root", "integrationtest", "192.168.99.100/ladder_mazebert");
    }

    @Override
    public DataSource getDataSource() {
        DataSource dataSource = super.getDataSource();

        clearAllTables();

        return dataSource;
    }

    private void clearAllTables() {
        clearTable("Player");
    }

    private void clearTable(String table) {
        QueryRunner runner = new QueryRunner(super.getDataSource());
        try {
            runner.update("DELETE FROM " + table + ";");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear table '" + table + "'", e);
        }
    }
}
