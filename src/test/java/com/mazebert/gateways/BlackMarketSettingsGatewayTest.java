package com.mazebert.gateways;

import com.mazebert.entities.BlackMarketSettings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class BlackMarketSettingsGatewayTest extends GatewayTest<BlackMarketSettingsGateway> {
    @Test
    public void getSettings_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.getSettings());
        thenGatewayErrorIs("Failed to read black market settings.");
    }

    @Test
    public void getSettings_propertiesAreFilled() {
        BlackMarketSettings settings = gateway.getSettings();

        assertNotNull(settings);
        assertEquals(250, settings.getPrice());
    }
}
