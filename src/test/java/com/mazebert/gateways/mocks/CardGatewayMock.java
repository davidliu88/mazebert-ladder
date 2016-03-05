package com.mazebert.gateways.mocks;

import com.mazebert.entities.Card;
import com.mazebert.entities.Hero;
import com.mazebert.entities.Item;
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
    public List<Hero> findAllHeroes() {
        List<Hero> heroes = new ArrayList<>();
        cards.stream().filter(card -> card instanceof Hero).forEach(card -> heroes.add((Hero)card));
        return heroes;
    }

    @Override
    public List<Item> findAllItems() {
        List<Item> items = new ArrayList<>();
        cards.stream().filter(card -> card instanceof Item).forEach(card -> items.add((Item) card));
        return items;
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
