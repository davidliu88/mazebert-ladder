package com.mazebert.gateways.mysql;

import com.mazebert.entities.Purchase;
import com.mazebert.gateways.PurchaseGateway;
import com.mazebert.gateways.error.GatewayError;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class MySqlPurchaseGateway extends MySqlGateway implements PurchaseGateway {
    @Inject
    public MySqlPurchaseGateway(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<String> findPurchasedProductIds(long playerId) {
        try {
            return getQueryRunner().query("SELECT productId FROM PlayerPurchasedProduct WHERE playerId=? ORDER BY productId ASC;",
                    new ColumnListHandler<>(1),
                    playerId);
        } catch (SQLException e) {
            throw new GatewayError("Failed to determine purchased product ids for player.", e);
        }
    }

    @Override
    public void addPurchase(Purchase purchase) {
        try {
            getQueryRunner().insert("INSERT INTO PlayerPurchasedProduct (playerId, productId, store, data, signature) VALUES(?, ?, ?, ?, ?);",
                    new ScalarHandler<>(),
                    purchase.getPlayerId(),
                    purchase.getProductId(),
                    purchase.getStore(),
                    purchase.getData(),
                    purchase.getSignature());
        } catch (SQLException e) {
            throw new GatewayError("Failed to create purchase entity.", e);
        }
    }
}
