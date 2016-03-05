package com.mazebert.gateways.mocks;

import com.mazebert.entities.*;
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
    public List<Potion> findAllPotions() {
        List<Potion> potions = new ArrayList<>();
        cards.stream().filter(card -> card instanceof Potion).forEach(card -> potions.add((Potion) card));
        return potions;
    }

    @Override
    public List<Tower> findAllTowers() {
        List<Tower> towers = new ArrayList<>();
        cards.stream().filter(card -> card instanceof Tower).forEach(card -> towers.add((Tower) card));
        return towers;
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
