package com.mazebert.gateways;

import com.mazebert.builders.BlackMarketOfferBuilder;
import com.mazebert.entities.BlackMarketOffer;
import com.mazebert.entities.CardType;
import com.mazebert.entities.Player;
import org.junit.Test;

import java.util.Date;

import static com.mazebert.builders.BuilderFactory.*;
import static org.junit.Assert.*;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.date;

public abstract class BlackMarketOfferGatewayTest extends GatewayTest<BlackMarketOfferGateway> {
    private Player player = a(player().casid());

    @Test
    public void addOffer_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.addOffer(a(blackMarketOffer())));
        thenGatewayErrorIs("Failed to add black market offer.");
    }

    @Test
    public void addOffer_offerIsAdded() {
        gateway.addOffer(a(blackMarketOffer()
                .withCardId(10)
                .withCardType(CardType.ITEM)
                .withExpirationDate(a(date().with("2016-02-14 22:00:00")))
        ));

        BlackMarketOffer offer = gateway.findLatestOffer();
        assertNotNull(offer);
        assertTrue(offer.getId() > 0);
        assertEquals(10, offer.getCardId());
        assertEquals(CardType.ITEM, offer.getCardType());
        assertEquals(a(date().with("2016-02-14 22:00:00")), offer.getExpirationDate());
    }

    @Test
    public void addOffer_idIsSet() {
        BlackMarketOffer offer = a(blackMarketOffer().withExpirationDate(new Date()));
        gateway.addOffer(offer);
        gateway.addOffer(offer);
        assertTrue(offer.getId() > 1);
    }

    @Test
    public void findLatestOffer_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findLatestOffer());
        thenGatewayErrorIs("Failed to find latest black market offer.");
    }

    @Test
    public void findLatestOffer_noOffers() {
        BlackMarketOffer offer = gateway.findLatestOffer();
        assertNull(offer);
    }

    @Test
    public void findLatestOffer_latestExpirationDate() {
        gateway.addOffer(a(blackMarketOffer()
                .withCardId(3)
                .withExpirationDate(a(date().with("2016-02-14 22:00:03")))
        ));
        gateway.addOffer(a(blackMarketOffer()
                .withCardId(2)
                .withExpirationDate(a(date().with("2016-02-14 22:00:02")))
        ));
        gateway.addOffer(a(blackMarketOffer()
                .withCardId(1)
                .withExpirationDate(a(date().with("2016-02-14 22:00:01")))
        ));

        BlackMarketOffer offer = gateway.findLatestOffer();
        assertEquals(3, offer.getCardId());
    }

    @Test
    public void isOfferPurchased_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.isOfferPurchased(a(defaultOffer()), player));
        thenGatewayErrorIs("Failed to determine if black market offer is purchased by player.");
    }

    @Test
    public void isOfferPurchased_noOfferPurchased() {
        assertFalse(gateway.isOfferPurchased(a(defaultOffer()), player));
    }

    @Test
    public void isOfferPurchased_offerPurchased() {
        gateway.markOfferAsPurchased(a(defaultOffer()), player);
        assertTrue(gateway.isOfferPurchased(a(defaultOffer()), player));
    }

    @Test
    public void isOfferPurchased_offerPurchasedByAnotherPlayer() {
        gateway.markOfferAsPurchased(a(defaultOffer()), a(player().casid().withId(12345)));
        assertFalse(gateway.isOfferPurchased(a(defaultOffer()), player));
    }

    @Test
    public void markOfferAsPurchased_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.markOfferAsPurchased(a(defaultOffer()), player));
        thenGatewayErrorIs("Failed to mark black market offer as purchased by player.");
    }

    private BlackMarketOfferBuilder defaultOffer() {
        return blackMarketOffer()
                .withCard(item().bowlingBall())
                .withExpirationDate(a(date().with("2015-10-10 20:00:00")));
    }
}
