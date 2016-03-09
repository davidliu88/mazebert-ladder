package com.mazebert.gateways.mocks;

import com.mazebert.entities.Card;
import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;
import com.mazebert.gateways.FoilCardGateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

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
    public int getFoilCardAmount(long playerId, long cardId, int cardType) {
        FoilCard card = findFoilCard(playerId, cardId, cardType);
        return card == null ? 0 : card.getAmount();
    }

    @Override
    public boolean isFoilCardOwnedByPlayer(long playerId, long cardId, int cardType) {
        return findFoilCard(playerId, cardId, cardType) != null;
    }

    @Override
    public void addFoilCardToPlayer(long playerId, FoilCard foilCard) {
        FoilCard card = findFoilCard(playerId, foilCard.getCardId(), foilCard.getCardType());
        if (card == null) {
            List<FoilCard> cards = new ArrayList<>();
            foilCardsForPlayer.put(playerId, cards);
            cards.add(foilCard);
        } else {
            card.setAmount(card.getAmount() + foilCard.getAmount());
        }
    }

    @Override
    public void setAmountOfAllPlayerFoilCards(long playerId, int amount) {
        getFoilCardsForPlayerId(playerId).stream().forEach(foilCard -> foilCard.setAmount(amount));
    }

    private FoilCard findFoilCard(long playerId, long cardId, int cardType) {
        List<FoilCard> cards = foilCardsForPlayer.get(playerId);
        if (cards != null) {
            for (FoilCard card : cards) {
                if (card.getCardId() == cardId && card.getCardType() == cardType) {
                    return card;
                }
            }
        }

        return null;
    }

    public void givenFoilCardsForPlayer(Player player, List<FoilCard> foilCards) {
        foilCardsForPlayer.put(player.getId(), foilCards);
    }

    public void thenFoilCardWasAddedToPlayer(Player player, Card expected) {
        FoilCard foilCard = findFoilCard(player.getId(), expected.getId(), expected.getType());
        assertNotNull(foilCard);
        assertEquals("Gateway expects +1 here so that the total amount is increased.", 1, foilCard.getAmount());
    }

    public void thenNoFoilCardsWereAddedToPlayer(Player player) {
        assertNull(foilCardsForPlayer.get(player.getId()));
    }
}
