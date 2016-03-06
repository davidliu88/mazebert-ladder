package com.mazebert.gateways;

import com.mazebert.entities.FoilCard;

import java.util.List;

public interface FoilCardGateway {
    List<FoilCard> getFoilCardsForPlayerId(long playerId);
    boolean isFoilCardOwnedByPlayer(long playerId, long cardId, int cardType);
    void addFoilCardToPlayer(long playerId, FoilCard foilCard);
    void setAmountOfAllPlayerFoilCards(long playerId, int amount);
}
