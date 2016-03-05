package com.mazebert.gateways.mocks;

import com.mazebert.entities.Card;
import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;
import com.mazebert.gateways.FoilCardGateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FoilCardGatewayMock implements FoilCardGateway {
    private Map<Long, List<FoilCard>> foilCardsForPlayer = new HashMap<>();

    @Override
    public List<FoilCard> getFoilCardsForPlayerId(long playerId) {
        List<FoilCard> cards = foilCardsForPlayer.get(playerId);
        if (cards == null) {
            cards = new ArrayList<>();
        }
        return cards;
    }

    @Override
    public boolean isFoilCardOwnedByPlayer(long playerId, long cardId, int cardType) {
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

    @Override
    public void addFoilCardToPlayer(long playerId, FoilCard foilCard) {
        List<FoilCard> cards = foilCardsForPlayer.get(playerId);
        if (cards == null) {
            cards = new ArrayList<>();
            foilCardsForPlayer.put(playerId, cards);
        }
        cards.add(foilCard);
    }

    public void givenFoilCardsForPlayer(Player player, List<FoilCard> foilCards) {
        foilCardsForPlayer.put(player.getId(), foilCards);
    }

    public void thenFoilCardWasAddedToPlayer(Player player, Card expected) {
        assertTrue(isFoilCardOwnedByPlayer(player.getId(), expected.getId(), expected.getType()));
    }

    public void thenNoFoilCardsWereAddedToPlayer(Player player) {
        assertNull(foilCardsForPlayer.get(player.getId()));
    }
}
