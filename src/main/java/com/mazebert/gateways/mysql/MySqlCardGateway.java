package com.mazebert.gateways.mysql;

import com.mazebert.entities.*;
import com.mazebert.gateways.CardGateway;
import com.mazebert.gateways.error.GatewayError;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
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

    @Override
    public List<Card> findAllCards() {
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(findAllTowers());
        allCards.addAll(findAllItems());
        allCards.addAll(findAllPotions());
        allCards.addAll(findAllHeroes());
        return allCards;
    }

    @Override
    public List<Hero> findAllHeroes() {
        return findAll(CardType.HERO);
    }

    @Override
    public List<Item> findAllItems() {
        return findAll(CardType.ITEM);
    }

    @Override
    public List<Potion> findAllPotions() {
        return findAll(CardType.POTION);
    }

    @Override
    public List<Tower> findAllTowers() {
        return findAll(CardType.TOWER);
    }

    @SuppressWarnings("unchecked")
    private <T extends Card> List<T> findAll(int cardType) {
        try {
            return getQueryRunner().query(FIND_CARD_QUERY + getTableName(cardType),
                    new BeanListHandler<>((Class<T>)getCardClass(cardType)));
        } catch (SQLException e) {
            throw new GatewayError("Failed to find all cards in table '" + getTableName(cardType) + "'.", e);
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
        try {
            List<Card> result = new ArrayList<>();
            QueryRunner queryRunner = getQueryRunner();
            addBlackMarketCards(queryRunner, CardType.ITEM, result);
            addBlackMarketCards(queryRunner, CardType.TOWER, result);
            addBlackMarketCards(queryRunner, CardType.POTION, result);
            addBlackMarketCards(queryRunner, CardType.HERO, result);
            return result;
        } catch (SQLException e) {
            throw new GatewayError("Failed to find black market cards.", e);
        }
    }

    private void addBlackMarketCards(QueryRunner queryRunner, int cardType, List<Card> result) throws SQLException {
        result.addAll(queryRunner.query(FIND_CARD_QUERY +
                getTableName(cardType) +
                " WHERE isBlackMarketOffer=1;",
                new BeanListHandler<>(getCardClass(cardType))));
    }
}
