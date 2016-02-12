package com.mazebert.gateways;

import com.mazebert.entities.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductGatewayCoach implements ProductGateway {
    private Map<Long, List<String>> purchasedProductIdsForPlayer = new HashMap<>();

    @Override
    public List<String> findPurchasedProductIds(long playerId) {
        return purchasedProductIdsForPlayer.get(playerId);
    }

    public void givenPurchasedProductIds(Player player, List<String> productIds) {
        purchasedProductIdsForPlayer.put(player.getId(), productIds);
    }
}
