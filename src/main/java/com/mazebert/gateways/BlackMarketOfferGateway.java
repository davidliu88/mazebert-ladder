package com.mazebert.gateways;

import com.mazebert.entities.BlackMarketOffer;
import com.mazebert.entities.Player;

public interface BlackMarketOfferGateway {
    BlackMarketOffer findLatestOffer();
    void addOffer(BlackMarketOffer offer);
    boolean isOfferPurchased(BlackMarketOffer offer, Player player);
    void markOfferAsPurchased(BlackMarketOffer offer, Player player);
}
