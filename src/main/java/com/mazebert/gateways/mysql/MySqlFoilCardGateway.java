package com.mazebert.gateways.mysql;

import com.mazebert.entities.FoilCard;
import com.mazebert.gateways.FoilCardGateway;

import java.util.List;

public class MySqlFoilCardGateway implements FoilCardGateway {
    @Override
    public List<FoilCard> getFoilCardsForPlayerId(long playerId) {
        return null;
    }

    @Override
    public boolean isFoilCardOwnedByPlayer(long playerId, int cardId, int cardType) {
        return false;
    }
}
