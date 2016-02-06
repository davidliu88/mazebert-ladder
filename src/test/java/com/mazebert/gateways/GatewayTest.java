package com.mazebert.gateways;

import com.mazebert.gateways.error.GatewayError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GatewayTest<Gateway> {
    protected Gateway gateway;
    protected Gateway errorGateway;

    protected GatewayError error;

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
