package com.mazebert.gateways;

import com.mazebert.entities.*;

import java.util.List;

public interface CardGateway {
    Card findCard(long cardId, int cardType);
    List<Hero> findAllHeroes();
    List<Item> findAllItems();
    List<Potion> findAllPotions();
    List<Tower> findAllTowers();
    List<Card> findAllBlackMarketCards();
}
