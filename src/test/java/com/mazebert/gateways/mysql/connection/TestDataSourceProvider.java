package com.mazebert.gateways.mysql.connection;

import com.mazebert.plugins.system.mocks.LoggerMock;
import org.apache.commons.dbutils.QueryRunner;
import org.jusecase.transaction.TransactionRunner;
import org.jusecase.transaction.simple.SimpleTransactionRunner;
import org.jusecase.transaction.simple.ThreadLocalTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

public class TestDataSourceProvider implements DataSourceProvider {
    public static final TestDataSourceProvider instance = new TestDataSourceProvider();
    private DataSourceProvider realProvider;
    private SimpleTransactionRunner transactionRunner;
    private LoggerMock logger = new LoggerMock();

    private TestDataSourceProvider() {
    }

    @Override
    public void prepare() {
        Credentials testCredentials = new Credentials("root", "integrationtest", resolveHost() + "/ladder_mazebert");
        transactionRunner = new SimpleTransactionRunner(new ThreadLocalTransactionManager());
        HikariDataSourceProvider hikariDataSourceProvider = new HikariDataSourceProvider(testCredentials, transactionRunner, logger.getLogger());
        hikariDataSourceProvider.setUnregisterDriverOnDisposal(false);

        realProvider = hikariDataSourceProvider;
        realProvider.prepare();
    }

    @Override
    public DataSource get() {
        if (realProvider == null) {
            prepare();
        }
        return realProvider.get();
    }

    @Override
    public void dispose() {
        realProvider.dispose();
        realProvider = null;
        transactionRunner = null;
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
            new QueryRunner(get()).update("TRUNCATE TABLE " + table + ";");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear table '" + table + "'", e);
        }
    }

    public TransactionRunner getTransactionRunner() {
        if (transactionRunner == null) {
            prepare();
        }
        return transactionRunner;
    }
}
