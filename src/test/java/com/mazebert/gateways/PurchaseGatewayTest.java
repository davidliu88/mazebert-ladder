package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.gateways.error.KeyAlreadyExists;
import org.junit.Test;

import static com.mazebert.builders.BuilderFactory.player;
import static com.mazebert.builders.BuilderFactory.purchase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.list;

public abstract class PurchaseGatewayTest extends GatewayTest<PurchaseGateway> {
    protected PlayerGateway playerGateway;

    @Test
    public void findPurchasedProductIds_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findPurchasedProductIds(1));
        thenGatewayErrorIs("Failed to determine purchased product ids for player.");
    }

    @Test
    public void findPurchasedProductIds_noPurchases() {
        Player player = a(player().casid());
        givenPlayerExists(player);
        assertEquals(a(list()), gateway.findPurchasedProductIds(player.getId()));
    }

    @Test
    public void findPurchasedProductIds_threePurchases() {
        Player player = a(player().casid());
        givenPlayerExists(player);
        gateway.addPurchase(a(purchase().googlePlayWhisky().withPlayerId(player.getId())));
        gateway.addPurchase(a(purchase().googlePlayCookie().withPlayerId(player.getId())));
        gateway.addPurchase(a(purchase().googlePlayBeer().withPlayerId(player.getId())));

        assertEquals(a(list(
                "com.mazebert.beer",
                "com.mazebert.cookie",
                "com.mazebert.whisky")), gateway.findPurchasedProductIds(player.getId()));
    }

    @Test
    public void addPurchase_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.addPurchase(a(purchase())));
        thenGatewayErrorIs("Failed to create purchase entity.");
    }

    @Test
    public void addPurchase_alreadyExists() {
        Player player = a(player().casid());
        givenPlayerExists(player);

        gateway.addPurchase(a(purchase().googlePlayWhisky().withPlayerId(player.getId())));
        try {
            gateway.addPurchase(a(purchase().googlePlayWhisky().withPlayerId(player.getId())));
        } catch (KeyAlreadyExists error) {
            this.error = error;
        }

        assertNotNull(error);
    }

    private void givenPlayerExists(Player player) {
        playerGateway.addPlayer(player);
    }
}