package com.mazebert.gateways;

import com.mazebert.entities.BlackMarketOffer;

public interface BlackMarketOfferGateway {
    BlackMarketOffer findLatestOffer();
    void addOffer(BlackMarketOffer offer);
}
