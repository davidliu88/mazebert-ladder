package com.mazebert.gateways.mocks;

import com.mazebert.entities.BlackMarketOffer;
import com.mazebert.entities.Card;
import com.mazebert.gateways.BlackMarketOfferGateway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BlackMarketOfferGatewayMock implements BlackMarketOfferGateway {
    private BlackMarketOffer latestOffer;
    private BlackMarketOffer lastAddedOffer;

    @Override
    public BlackMarketOffer findLatestOffer() {
        return latestOffer;
    }

    @Override
    public void addOffer(BlackMarketOffer offer) {
        latestOffer = lastAddedOffer = offer;
    }

    public void givenLatestOffer(BlackMarketOffer blackMarketOffer) {
        latestOffer = blackMarketOffer;
    }

    public void thenOfferWasCreatedForCard(Card card) {
        assertNotNull(lastAddedOffer);
        assertEquals(card.getId(), lastAddedOffer.getCardId());
        assertEquals(card.getType(), lastAddedOffer.getCardType());
    }
}
