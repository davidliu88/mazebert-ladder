package com.mazebert.gateways;

import com.mazebert.entities.Player;
import com.mazebert.entities.Purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseGatewayCoach implements PurchaseGateway {
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
