package com.mazebert.gateways.mocks;

import com.mazebert.entities.BlackMarketOffer;
import com.mazebert.entities.Card;
import com.mazebert.entities.Player;
import com.mazebert.gateways.BlackMarketOfferGateway;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BlackMarketOfferGatewayMock implements BlackMarketOfferGateway {
    private BlackMarketOffer latestOffer;
    private BlackMarketOffer lastAddedOffer;
    private Map<Long, List<BlackMarketOffer>> purchasedOffers = new HashMap<>();

    @Override
    public BlackMarketOffer findLatestOffer() {
        return latestOffer;
    }

    @Override
    public void addOffer(BlackMarketOffer offer) {
        latestOffer = lastAddedOffer = offer;
    }

    @Override
    public boolean isOfferPurchased(final BlackMarketOffer offer, Player player) {
        List<BlackMarketOffer> offers = purchasedOffers.get(player.getId());
        if (offers == null) {
            return false;
        }

        return offers.stream().anyMatch(blackMarketOffer -> blackMarketOffer.getId() == offer.getId());
    }

    @Override
    public void markOfferAsPurchased(BlackMarketOffer offer, Player player) {
        if (!purchasedOffers.containsKey(player.getId())) {
            purchasedOffers.put(player.getId(), new ArrayList<>());
        }
        purchasedOffers.get(player.getId()).add(offer);
    }

    public void givenLatestOffer(BlackMarketOffer blackMarketOffer) {
        latestOffer = blackMarketOffer;
    }

    public void givenOfferIsPurchased(BlackMarketOffer offer, Player player) {
        markOfferAsPurchased(offer, player);
    }

    public void thenOfferWasCreatedForCard(Card card) {
        assertEquals(card.getId(), lastAddedOffer.getCardId());
        assertEquals(card.getType(), lastAddedOffer.getCardType());
    }

    public void thenOfferWasCreatedWithExpirationDate(Date date) {
        assertEquals(date, lastAddedOffer.getExpirationDate());
    }

    public void thenNoOfferWasCreated() {
        assertNull(lastAddedOffer);
    }
}
