package com.mazebert.gateways.mysql;

import com.mazebert.entities.BlackMarketOffer;
import com.mazebert.entities.Player;
import com.mazebert.gateways.BlackMarketOfferGateway;

public class MySqlBlackMarketOfferGateway implements BlackMarketOfferGateway {
    @Override
    public BlackMarketOffer findLatestOffer() {
        return null;
    }

    @Override
    public void addOffer(BlackMarketOffer offer) {
    }

    @Override
    public boolean isOfferPurchased(BlackMarketOffer offer, Player player) {
        return false;
    }
}
