package com.mazebert.gateways.mocks;

import com.mazebert.entities.BlackMarketOffer;
import com.mazebert.gateways.BlackMarketOfferGateway;

public class BlackMarketOfferGatewayMock implements BlackMarketOfferGateway {
    private BlackMarketOffer latestOffer;

    @Override
    public BlackMarketOffer findLatestOffer() {
        return latestOffer;
    }

    @Override
    public void addOffer(BlackMarketOffer offer) {
        latestOffer = offer;
    }

    public void givenLatestOffer(BlackMarketOffer blackMarketOffer) {
        latestOffer = blackMarketOffer;
    }
}
