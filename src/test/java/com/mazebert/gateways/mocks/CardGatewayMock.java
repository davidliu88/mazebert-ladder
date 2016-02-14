package com.mazebert.gateways.mocks;

import com.mazebert.entities.Card;
import com.mazebert.gateways.CardGateway;

import java.util.ArrayList;
import java.util.List;

public class CardGatewayMock implements CardGateway {
    private List<Card> cards = new ArrayList<>();

    @Override
    public Card findCard(long cardId, int cardType) {
        try {
            return cards.stream().filter(card -> card.getId() == cardId && card.getType() == cardType).findFirst().get();
        } catch (Exception e) {
            return null;
        }
    }

    public void givenCardExists(Card card) {
        cards.clear();
        cards.add(card);
    }
}
