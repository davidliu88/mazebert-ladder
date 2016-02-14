package com.mazebert.gateways.mysql;

import com.mazebert.entities.BlackMarketOffer;
import com.mazebert.gateways.BlackMarketOfferGateway;

public class MySqlBlackMarketOfferGateway implements BlackMarketOfferGateway {
    @Override
    public BlackMarketOffer findLatestOffer() {
        return null;
    }

    @Override
    public void addOffer(BlackMarketOffer offer) {
    }
}
