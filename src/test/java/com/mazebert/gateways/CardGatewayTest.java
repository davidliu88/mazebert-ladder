package com.mazebert.gateways;

import com.mazebert.entities.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class CardGatewayTest extends GatewayTest<CardGateway> {
    @Test
    public void findCard_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findCard(1, CardType.ITEM));
        thenGatewayErrorIs("Failed to find card.");
    }

    @Test
    public void findCard_doesNotExist() {
        Card card = gateway.findCard(9999, CardType.ITEM);
        assertNull(card);
    }

    @Test
    public void findCard_propertiesAreSet() {
        Card card = gateway.findCard(1, CardType.ITEM);
        thenCardIsWoodenStaff(card);
    }

    @Test
    public void findCard_blackMarketOfferIsSet() {
        Card card = gateway.findCard(54, CardType.ITEM);
        assertEquals(true, card.isBlackMarketOffer());
    }

    @Test
    public void findCard_item() {
        Card card = gateway.findCard(1, CardType.ITEM);
        thenCardIsOfType(card, Item.class, CardType.ITEM);
    }

    @Test
    public void findCard_tower() {
        Card card = gateway.findCard(1, CardType.TOWER);
        thenCardIsOfType(card, Tower.class, CardType.TOWER);
    }

    @Test
    public void findCard_potion() {
        Card card = gateway.findCard(1, CardType.POTION);
        thenCardIsOfType(card, Potion.class, CardType.POTION);
    }

    @Test
    public void findCard_hero() {
        Card card = gateway.findCard(1, CardType.HERO);
        thenCardIsOfType(card, Hero.class, CardType.HERO);
    }

    private void thenCardIsWoodenStaff(Card card) {
        assertEquals(1, card.getId());
        assertEquals("Wooden Staff", card.getName());
        assertEquals("0.2", card.getSinceVersion());
        assertEquals(true, card.isForgeable());
        assertEquals(false, card.isBlackMarketOffer());
    }

    private void thenCardIsOfType(Card card, Class<? extends Card> expectedClass, int expectedType) {
        assertEquals(expectedClass, card.getClass());
        assertEquals(expectedType, card.getType());
    }
}