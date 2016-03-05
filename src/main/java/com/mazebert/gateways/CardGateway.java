package com.mazebert.gateways;

import com.mazebert.entities.Card;
import com.mazebert.entities.Hero;
import com.mazebert.entities.Item;
import com.mazebert.entities.Potion;

import java.util.List;

public interface CardGateway {
    Card findCard(long cardId, int cardType);
    List<Hero> findAllHeroes();
    List<Item> findAllItems();
    List<Potion> findAllPotions();
    List<Card> findAllBlackMarketCards();
}
