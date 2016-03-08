package com.mazebert.gateways.mysql.connection;

import com.mazebert.gateways.transaction.datasource.DataSourceTransactionManager;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.SQLException;
import java.util.Properties;

import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

public class TestDataSourceProvider extends C3p0DataSourceProvider {
    public static final TestDataSourceProvider instance = new TestDataSourceProvider();

    private TestDataSourceProvider() {
        super(new Credentials("root", "integrationtest", resolveHost() + "/ladder_mazebert"), new DataSourceTransactionManager());
        prepare();
    }

    private static String resolveHost() {
        String defaultHost = "localhost";
        Properties properties = new Properties();
        try {
            properties.load(a(inputStream().withResource("integrationtest-database.properties")));
            return properties.getProperty("host", defaultHost);
        } catch (Throwable e) {
            return defaultHost;
        }
    }

    public void clearTable(String table) {
        try {
            new QueryRunner(super.get()).update("TRUNCATE TABLE " + table + ";");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear table '" + table + "'", e);
        }
    }
}
