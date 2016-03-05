package com.mazebert.gateways;

import com.mazebert.entities.Card;
import com.mazebert.entities.Hero;
import com.mazebert.entities.Item;

import java.util.List;

public interface CardGateway {
    Card findCard(long cardId, int cardType);
    List<Hero> findAllHeroes();
    List<Item> findAllItems();
    List<Card> findAllBlackMarketCards();
}
