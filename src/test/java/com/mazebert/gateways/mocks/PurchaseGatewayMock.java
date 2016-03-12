package com.mazebert.gateways.mocks;

import com.mazebert.entities.Player;
import com.mazebert.entities.Purchase;
import com.mazebert.gateways.PurchaseGateway;
import com.mazebert.gateways.error.KeyAlreadyExists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseGatewayMock implements PurchaseGateway {
    private Map<Long, List<String>> purchasedProductIdsForPlayer = new HashMap<>();
    private Map<Long, List<Purchase>> purchasedProductsForPlayer = new HashMap<>();

    @Override
    public List<String> findPurchasedProductIds(long playerId) {
        return purchasedProductIdsForPlayer.get(playerId);
    }

    @Override
    public void addPurchase(Purchase purchase) {
        List<Purchase> purchases = purchasedProductsForPlayer.get(purchase.getPlayerId());
        if (purchases == null) {
            purchases = new ArrayList<>();
            purchasedProductsForPlayer.put(purchase.getPlayerId(), purchases);
        }

        for (Purchase existing : purchases) {
            if (existing.getPlayerId() == purchase.getPlayerId() &&
                existing.getProductId().equals(purchase.getProductId())) {
                throw new KeyAlreadyExists();
            }
        }

        purchases.add(purchase);
    }

    public void givenPurchasedProductIds(Player player, List<String> productIds) {
        purchasedProductIdsForPlayer.put(player.getId(), productIds);
    }

    public List<Purchase> getPurchasesForPlayer(Player player) {
        List<Purchase> purchases = purchasedProductsForPlayer.get(player.getId());
        return purchases == null ? new ArrayList<>() : purchases;
    }
}
