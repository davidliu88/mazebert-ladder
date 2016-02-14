package com.mazebert.gateways.mocks;

import com.mazebert.entities.BlackMarketSettings;
import com.mazebert.gateways.BlackMarketSettingsGateway;

public class BlackMarketSettingsGatewayMock implements BlackMarketSettingsGateway {
    private BlackMarketSettings settings;

    @Override
    public BlackMarketSettings getSettings() {
        return settings;
    }

    public void givenSettings(BlackMarketSettings settings) {
        this.settings = settings;
    }
}
