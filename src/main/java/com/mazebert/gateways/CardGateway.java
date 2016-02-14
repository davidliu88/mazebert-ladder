package com.mazebert.gateways;

import com.mazebert.entities.Card;

import java.util.List;

public interface CardGateway {
    Card findCard(long cardId, int cardType);

    List<Card> findAllBlackMarketCards();
}
