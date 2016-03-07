package com.mazebert.gateways.mysql;

import com.mazebert.entities.FoilCard;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.error.GatewayError;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class MySqlFoilCardGateway extends MySqlGateway implements FoilCardGateway {
    @Inject
    public MySqlFoilCardGateway(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<FoilCard> getFoilCardsForPlayerId(long playerId) {
        try {
            return getQueryRunner().query("SELECT cardId, cardType, amount FROM PlayerFoilCard WHERE playerId=?;",
                    new BeanListHandler<>(FoilCard.class),
                    playerId);
        } catch (SQLException e) {
            throw new GatewayError("Failed to get foil cards for player.", e);
        }
    }

    @Override
    public int getFoilCardAmount(long playerId, long cardId, int cardType) {
        // TODO implement me!
        return 0;
    }

    @Override
    public boolean isFoilCardOwnedByPlayer(long playerId, long cardId, int cardType) {
        try {
            Integer count = getQueryRunner().query("SELECT amount FROM PlayerFoilCard WHERE playerId=? AND cardId=? AND cardType=?;",
                    new ScalarHandler<>(),
                    playerId,
                    cardId,
                    cardType);
            return count != null && count > 0;
        } catch (SQLException e) {
            throw new GatewayError("Failed to determine if foilcard is owned by player.", e);
        }
    }

    @Override
    public void addFoilCardToPlayer(long playerId, FoilCard foilCard) {
        try {
            getQueryRunner().insert("INSERT INTO PlayerFoilCard (playerId, cardId, cardType, amount) VALUES (?, ?, ?, ?);",
                    new ScalarHandler<>(),
                    playerId,
                    foilCard.getCardId(),
                    foilCard.getCardType(),
                    foilCard.getAmount());
        } catch (SQLException e) {
            throw new GatewayError("Failed to add foil card to player.", e);
        }

        // TODO update instead of insert if foil card already exists! (catch unique constraint exception internally)
    }

    @Override
    public void setAmountOfAllPlayerFoilCards(long playerId, int amount) {
        // TODO implement me!
    }
}
