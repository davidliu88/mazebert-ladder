package com.mazebert.gateways;

import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoilCardGatewayCoach implements FoilCardGateway {
    private Map<Long, List<FoilCard>> foilCardsForPlayer = new HashMap<>();

    @Override
    public List<FoilCard> getFoilCardsForPlayerId(long playerId) {
        return foilCardsForPlayer.get(playerId);
    }

    @Override
    public boolean isFoilCardOwnedByPlayer(long playerId, int cardId, int cardType) {
        List<FoilCard> cards = foilCardsForPlayer.get(playerId);
        if (cards != null) {
            for (FoilCard card : cards) {
                if (card.getCardId() == cardId && card.getCardType() == cardType) {
                    return true;
                }
            }
        }

        return false;
    }

    public void givenFoilCardsForPlayer(Player player, List<FoilCard> foilCards) {
        foilCardsForPlayer.put(player.getId(), foilCards);
    }
}
