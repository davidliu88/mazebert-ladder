package com.mazebert.gateways;

import java.util.List;

public interface ProductGateway {
    List<String> findPurchasedProductIds(long playerId);
}
