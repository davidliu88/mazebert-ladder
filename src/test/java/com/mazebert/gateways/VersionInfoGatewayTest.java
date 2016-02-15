package com.mazebert.gateways;

import com.mazebert.entities.VersionInfo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class VersionInfoGatewayTest extends GatewayTest<VersionInfoGateway> {
    @Test
    public void findVersionInfo_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findVersionInfo("GooglePlay"));
        thenGatewayErrorIs("Failed to find version info for store.");
    }

    @Test
    public void findVersionInfo_unknownStore() {
        VersionInfo versionInfo = gateway.findVersionInfo("Unknown");
        assertNull(versionInfo);
    }

    @Test
    public void findVersionInfo_propertiesAreFilled() {
        VersionInfo versionInfo = gateway.findVersionInfo("GooglePlay");

        assertEquals("GooglePlay", versionInfo.getStore());
        assertEquals("1.2.0", versionInfo.getVersion());
        assertEquals("market://details?id=air.com.mazebert.MazebertTD", versionInfo.getUrl());
        assertEquals("New wizard skills!\n\nA new black market item!\n\nSeveral bugfixes..", versionInfo.getDetails());
    }
}
