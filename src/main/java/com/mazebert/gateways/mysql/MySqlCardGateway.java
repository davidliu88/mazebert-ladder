package com.mazebert.gateways.mysql;

import com.mazebert.entities.Card;
import com.mazebert.gateways.CardGateway;

import java.util.List;

public class MySqlCardGateway implements CardGateway {
    @Override
    public Card findCard(long cardId, int cardType) {
        return null;
    }

    @Override
    public List<Card> findAllBlackMarketCards() {
        return null;
    }
}
