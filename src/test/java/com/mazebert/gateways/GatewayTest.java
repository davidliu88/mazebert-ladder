package com.mazebert.gateways;

import com.mazebert.categories.ParallelIntegrationTest;
import com.mazebert.gateways.error.GatewayError;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.jusecase.transaction.rules.RunAsTransactionAndRollback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Category(ParallelIntegrationTest.class)
public class GatewayTest<Gateway> {
    protected Gateway gateway;
    protected Gateway errorGateway;

    protected GatewayError error;

    @Rule
    public RunAsTransactionAndRollback runAsTransactionAndRollback = new RunAsTransactionAndRollback(TestDataSourceProvider.instance.getTransactionRunner());

    protected void whenGatewayErrorIsForced(Runnable runnable) {
        try {
            runnable.run();
        } catch (GatewayError error) {
            this.error = error;
        }
    }

    protected void thenGatewayErrorIs(String errorMessage) {
        assertNotNull("Gateway error expected, but received null", error);
        assertEquals(errorMessage, error.getMessage());
        assertNotNull("Gateway error must always provide a cause", error.getCause());
    }
}
