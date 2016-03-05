package com.mazebert.gateways;

import com.mazebert.entities.*;
import org.junit.Test;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.tower;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.jusecase.Builders.a;

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

        assertEquals(1, card.getId());
        assertEquals("Wooden Staff", card.getName());
        assertEquals("0.2", card.getSinceVersion());
        assertEquals(true, card.isForgeable());
        assertEquals(false, card.isBlackMarketOffer());
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

    @Test
    public void findAllBlackMarketCards_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findAllBlackMarketCards());
        thenGatewayErrorIs("Failed to find black market cards.");
    }

    @Test
    public void findAllBlackMarketCards_blackMarketItemsAreAdded() {
        List<Card> cards = gateway.findAllBlackMarketCards();

        assertEquals(6, cards.size());
        for (Card card : cards) {
            assertEquals(true, card.isBlackMarketOffer());
        }
    }

    @Test
    public void findAllTowers_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.findAllTowers());
        thenGatewayErrorIs("Failed to find all cards in table 'TOWER'.");
    }

    @Test
    public void findAllTowers() {
        thenPropertiesAreMappedCorrectly(a(tower().herbWitch()), gateway.findAllTowers());
    }

    private void thenCardIsOfType(Card card, Class<? extends Card> expectedClass, int expectedType) {
        assertEquals(expectedClass, card.getClass());
        assertEquals(expectedType, card.getType());
    }

    private void thenPropertiesAreMappedCorrectly(Card expected, List<? extends Card> cards) {
        for (Card card : cards) {
            if (card.getId() == expected.getId()) {
                assertEquals(expected.getType(), card.getType());
                assertEquals(expected.getName(), card.getName());
                assertEquals(expected.getRarity(), card.getRarity());
                assertEquals(expected.getSinceVersion(), card.getSinceVersion());
                return;
            }
        }

        fail("Expected card '" + expected.getName() + "' was not found in card list fetched from database.");
    }
}