package com.mazebert.gateways.mysql.connection;

import com.google.inject.Provider;
import com.mazebert.error.InternalServerError;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
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
import java.util.Set;
import java.util.logging.Logger;

@Singleton
public class C3p0DataSourceProvider implements DataSourceProvider, Provider<DataSource> {
    private final Credentials credentials;
    private final SimpleTransactionRunner transactionRunner;
    private final Logger logger;

    private ComboPooledDataSource dataSource;
    private DataSourceProxy dataSourceProxy;
    private boolean unregisterDriverOnDisposal = true;

    @Inject
    public C3p0DataSourceProvider(Credentials credentials, SimpleTransactionRunner transactionRunner, Logger logger) {
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
        waitForC3p0ToBeClosed();
        dataSource = null;

        if (isUnregisterDriverOnDisposal()) {
            unregisterDatabaseDrivers();
        }
        shutDownJdbcCleanUpThread();

        logger.info("Dispose data source complete.");
    }

    private void waitForC3p0ToBeClosed() {
        try {
            Thread thread = getC3p0DestroyThread();
            if (thread != null) {
                thread.join();
            } else {
                logger.info("Could not find C3P0 Resource Destroyer thread. Either the thread was already stopped, or it was renamed by a newer version of C3P0.");
            }
        } catch (Throwable throwable) {
            logger.warning("Waiting for C3P0 shutdown was interrupted (" + throwable.getMessage() + ").");
        }
    }

    private Thread getC3p0DestroyThread() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread thread : threadSet) {
            if (thread.isDaemon() && thread.getName().equals("Resource Destroyer in BasicResourcePool.close()")) {
                return thread;
            }
        }
        return null;
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
            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://" + credentials.getUrl() + "?autoReconnect=true");
            dataSource.setUser(credentials.getUser());
            dataSource.setPassword(credentials.getPassword());
            dataSource.setMinPoolSize(3);
            dataSource.setMaxPoolSize(30);
            dataSource.setAcquireIncrement(1);
            dataSource.setTestConnectionOnCheckin(false);
            dataSource.setTestConnectionOnCheckout(true);

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
    }

    public boolean isUnregisterDriverOnDisposal() {
        return unregisterDriverOnDisposal;
    }

    public void setUnregisterDriverOnDisposal(boolean unregisterDriverOnDisposal) {
        this.unregisterDriverOnDisposal = unregisterDriverOnDisposal;
    }
}
