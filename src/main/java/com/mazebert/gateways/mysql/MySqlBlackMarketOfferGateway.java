package com.mazebert.gateways.mysql;

import com.mazebert.entities.BlackMarketOffer;
import com.mazebert.entities.Player;
import com.mazebert.gateways.BlackMarketOfferGateway;
import com.mazebert.gateways.error.GatewayError;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;

public class MySqlBlackMarketOfferGateway implements BlackMarketOfferGateway {
    private final DataSource dataSource;

    @Inject
    public MySqlBlackMarketOfferGateway(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public BlackMarketOffer findLatestOffer() {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query("SELECT id, cardId, cardType, expirationDate FROM BlackMarketOffer ORDER BY expirationDate DESC LIMIT 1;",
                    new BeanHandler<>(BlackMarketOffer.class));
        } catch (SQLException e) {
            throw new GatewayError("Failed to find latest black market offer.", e);
        }
    }

    @Override
    public void addOffer(BlackMarketOffer offer) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            long id = queryRunner.insert("INSERT INTO BlackMarketOffer (cardId, cardType, expirationDate) VALUES(?, ?, ?);",
                    new ScalarHandler<>(),
                    offer.getCardId(),
                    offer.getCardType(),
                    offer.getExpirationDate());
            offer.setId(id);
        } catch (SQLException e) {
            throw new GatewayError("Failed to add black market offer.", e);
        }
    }

    @Override
    public boolean isOfferPurchased(BlackMarketOffer offer, Player player) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            long count = queryRunner.query("SELECT COUNT(*) FROM PlayerPurchasedBlackMarketOffer WHERE playerId=? AND offerId=?;",
                    new ScalarHandler<>(),
                    player.getId(),
                    offer.getId());
            return count > 0;
        } catch (SQLException e) {
            throw new GatewayError("Failed to determine if black market offer is purchased by player.", e);
        }
    }

    @Override
    public void markOfferAsPurchased(BlackMarketOffer offer, Player player) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.insert("INSERT INTO PlayerPurchasedBlackMarketOffer (playerId, offerId) VALUES(?, ?);",
                    new ScalarHandler<>(),
                    player.getId(),
                    offer.getId());
        } catch (SQLException e) {
            throw new GatewayError("Failed to mark black market offer as purchased by player.", e);
        }
    }
}
