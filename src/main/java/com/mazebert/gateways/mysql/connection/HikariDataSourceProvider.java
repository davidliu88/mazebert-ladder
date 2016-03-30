package com.mazebert.gateways.mysql.connection;

import com.google.inject.Provider;
import com.mazebert.error.InternalServerError;
import com.mazebert.gateways.mysql.MySqlBonusTimeGateway;
import com.mazebert.usecases.bonustime.FixBonusTimesWithZeroMapId;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.jusecase.transaction.simple.SimpleTransactionRunner;
import org.jusecase.transaction.simple.jdbc.DataSourceProxy;
import org.jusecase.transaction.simple.jdbc.DataSourceTransactionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Logger;

@Singleton
public class HikariDataSourceProvider implements DataSourceProvider, Provider<DataSource> {
    private final Credentials credentials;
    private final SimpleTransactionRunner transactionRunner;
    private final Logger logger;

    private HikariDataSource dataSource;
    private DataSourceProxy dataSourceProxy;
    private boolean unregisterDriverOnDisposal = true;

    @Inject
    public HikariDataSourceProvider(Credentials credentials, SimpleTransactionRunner transactionRunner, Logger logger) {
        this.credentials = credentials;
        this.transactionRunner = transactionRunner;
        this.logger = logger;
    }

    @Override
    public DataSource get() {
        return dataSourceProxy;
    }

    @Override
    public void prepare() {
        logger.info("Prepare data source.");

        createDataSource();
        prepareDatabase();

        logger.info("Prepare data source complete.");
    }

    @Override
    public void dispose() {
        logger.info("Dispose data source.");

        dataSource.close();
        dataSource = null;

        if (isUnregisterDriverOnDisposal()) {
            unregisterDatabaseDrivers();
        }
        shutDownJdbcCleanUpThread();

        logger.info("Dispose data source complete.");
    }

    private void unregisterDatabaseDrivers() {
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            final Driver driver = drivers.nextElement();
            if (this.getClass().getClassLoader().equals(getClass().getClassLoader())) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                    logger.warning("Failed to unregister JDBC driver with name '" + driver.toString() + "'.");
                }
            }
        }
    }

    private void shutDownJdbcCleanUpThread() {
        try {
            AbandonedConnectionCleanupThread.shutdown();
        }
        catch (InterruptedException e) {
            logger.warning("Failed to shutdown AbandonedConnectionCleanupThread: " + e.getMessage());
        }
    }

    private void createDataSource() {
        try {
            dataSource = new HikariDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://" + credentials.getUrl() + "?autoReconnect=true");
            dataSource.setUsername(credentials.getUser());
            dataSource.setPassword(credentials.getPassword());

            DataSourceTransactionFactory transactionFactory = new DataSourceTransactionFactory(dataSource, transactionRunner.getTransactionManager());
            transactionRunner.setTransactionFactory(transactionFactory);
            dataSourceProxy = transactionFactory.createProxy();
        } catch (Throwable e) {
            throw new InternalServerError("Database connection pool could not be initialized", e);
        }
    }

    private void prepareDatabase() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();

        // Temporary preparation step.
        FixBonusTimesWithZeroMapId fixBonusTimesWithZeroMapId = new FixBonusTimesWithZeroMapId(new MySqlBonusTimeGateway(dataSource), Logger.getLogger("fix-bonus-times"));
        fixBonusTimesWithZeroMapId.execute(null);
    }

    public boolean isUnregisterDriverOnDisposal() {
        return unregisterDriverOnDisposal;
    }

    public void setUnregisterDriverOnDisposal(boolean unregisterDriverOnDisposal) {
        this.unregisterDriverOnDisposal = unregisterDriverOnDisposal;
    }
}
