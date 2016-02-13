package com.mazebert.gateways.mysql;

import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.inputStream;

public class TestDataSourceProvider extends C3p0DataSourceProvider {
    public static final TestDataSourceProvider instance = new TestDataSourceProvider();

    private TestDataSourceProvider() {
        super("root", "integrationtest", resolveHost() + "/ladder_mazebert");
    }

    private static String resolveHost() {
        String defaultHost = "localhost";
        Properties properties = new Properties();
        try {
            properties.load(a(inputStream().withTestResource("integrationtest-database.properties")));
            return properties.getProperty("host", defaultHost);
        } catch (Throwable e) {
            return defaultHost;
        }
    }

    @Override
    public DataSource getDataSource() {
        DataSource dataSource = super.getDataSource();

        clearAllTables();

        return dataSource;
    }

    private void clearAllTables() {
        clearTable("Player");
        clearTable("PlayerPurchasedProduct");
        clearTable("PlayerFoilCard");
        clearTable("PlayerDailyQuest");
        clearTable("PlayerHiddenQuest");
    }

    private void clearTable(String table) {
        QueryRunner runner = new QueryRunner(super.getDataSource());
        try {
            runner.update("TRUNCATE TABLE " + table + ";");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear table '" + table + "'", e);
        }
    }
}
