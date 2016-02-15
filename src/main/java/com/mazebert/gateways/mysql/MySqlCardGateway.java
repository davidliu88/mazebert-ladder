package com.mazebert.gateways.mysql;

import com.mazebert.entities.*;
import com.mazebert.gateways.CardGateway;
import com.mazebert.gateways.error.GatewayError;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class MySqlCardGateway extends MySqlGateway implements CardGateway {
    private static final String FIND_CARD_QUERY = "SELECT id, name, rarity, sinceVersion, isForgeable AS forgeable, isBlackMarketOffer AS blackMarketOffer FROM ";

    @Inject
    public MySqlCardGateway(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Card findCard(long cardId, int cardType) {
        try {
            return getQueryRunner().query(FIND_CARD_QUERY + getTableName(cardType) + " WHERE id=?;",
                    new BeanHandler<>(getCardClass(cardType)),
                    cardId);
        } catch (SQLException e) {
            throw new GatewayError("Failed to find card.", e);
        }
    }

    private String getTableName(int cardType) {
        switch (cardType) {
            case CardType.ITEM: return "Item";
            case CardType.TOWER: return "Tower";
            case CardType.POTION: return "Potion";
            case CardType.HERO: return "Hero";
        }
        return null;
    }

    private Class<? extends Card> getCardClass(int cardType) {
        switch (cardType) {
            case CardType.ITEM: return Item.class;
            case CardType.TOWER: return Tower.class;
            case CardType.POTION: return Potion.class;
            case CardType.HERO: return Hero.class;
        }
        return null;
    }

    @Override
    public List<Card> findAllBlackMarketCards() {
        return null;
    }
}
