package com.mazebert.gateways.mysql;

import com.mazebert.entities.Card;
import com.mazebert.gateways.CardGateway;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.List;

public class MySqlCardGateway extends MySqlGateway implements CardGateway {
    @Inject
    public MySqlCardGateway(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Card findCard(long cardId, int cardType) {
        return null;
    }

    @Override
    public List<Card> findAllBlackMarketCards() {
        return null;
    }
}
