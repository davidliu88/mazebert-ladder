package com.mazebert.gateways;

import com.mazebert.entities.Purchase;

import java.util.List;

public interface PurchaseGateway {
    List<String> findPurchasedProductIds(long playerId);

    void addPurchase(Purchase purchase);
}
