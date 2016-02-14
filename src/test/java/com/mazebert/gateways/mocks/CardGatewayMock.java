package com.mazebert.gateways.mocks;

import com.mazebert.entities.Card;
import com.mazebert.gateways.CardGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<Card> findAllBlackMarketCards() {
        return cards.stream().filter(Card::isBlackMarketOffer).collect(Collectors.toList());
    }

    public void givenCardExists(Card card) {
        cards.clear();
        cards.add(card);
    }

    public void givenNoCardsExist() {
        cards.clear();
    }

    public void givenCardsExist(List<Card> cards) {
        this.cards.clear();
        this.cards.addAll(cards);
    }
}
