package com.mazebert.gateways.mocks;

import com.mazebert.entities.Player;
import com.mazebert.entities.Purchase;
import com.mazebert.gateways.PurchaseGateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseGatewayMock implements PurchaseGateway {
    private Map<Long, List<String>> purchasedProductIdsForPlayer = new HashMap<>();

    @Override
    public List<String> findPurchasedProductIds(long playerId) {
        return purchasedProductIdsForPlayer.get(playerId);
    }

    @Override
    public void addPurchase(Purchase purchase) {
    }

    public void givenPurchasedProductIds(Player player, List<String> productIds) {
        purchasedProductIdsForPlayer.put(player.getId(), productIds);
    }
}
