package com.mazebert.gateways;

import com.mazebert.entities.CardType;
import com.mazebert.entities.FoilCard;
import org.junit.Test;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.foilCard;
import static org.junit.Assert.*;
import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.listWith;

public abstract class FoilCardGatewayTest extends GatewayTest<FoilCardGateway> {


    @Test
    public void getFoilCardsForPlayerId_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.getFoilCardsForPlayerId(1));
        thenGatewayErrorIs("Failed to get foil cards for player.");
    }

    @Test
    public void getFoilCardsForPlayerId_noFoilCards() {
        assertEquals(a(listWith()), gateway.getFoilCardsForPlayerId(1));
    }

    @Test
    public void getFoilCardsForPlayerId_oneFoilCard() {
        gateway.addFoilCardToPlayer(1, a(foilCard().one().bowlingBall()));
        gateway.addFoilCardToPlayer(2, a(foilCard().one().hero()));

        List<FoilCard> foilCards = gateway.getFoilCardsForPlayerId(1);

        assertEquals(1, foilCards.size());
        assertEquals(58, foilCards.get(0).getCardId());
        assertEquals(CardType.ITEM, foilCards.get(0).getCardType());
        assertEquals(1, foilCards.get(0).getAmount());
    }

    @Test
    public void isFoilCardOwnedByPlayer_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.isFoilCardOwnedByPlayer(1, 2, 3));
        thenGatewayErrorIs("Failed to determine if foilcard is owned by player.");
    }

    @Test
    public void isFoilCardOwnedByPlayer_notOwned() {
        assertFalse(gateway.isFoilCardOwnedByPlayer(1, 1, CardType.TOWER));
    }

    @Test
    public void isFoilCardOwnedByPlayer_ownsOneCard() {
        FoilCard card = a(foilCard().one().bowlingBall());
        gateway.addFoilCardToPlayer(1, card);

        assertTrue(gateway.isFoilCardOwnedByPlayer(1, card.getCardId(), card.getCardType()));
    }

    @Test
    public void isFoilCardOwnedByPlayer_ownsOneCardManyTimes() {
        FoilCard card = a(foilCard().one().bowlingBall().withAmount(10));
        gateway.addFoilCardToPlayer(1, card);

        assertTrue(gateway.isFoilCardOwnedByPlayer(1, card.getCardId(), card.getCardType()));
    }

    @Test
    public void addFoilCardToPlayer_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.addFoilCardToPlayer(1, a(foilCard())));
        thenGatewayErrorIs("Failed to add foil card to player.");
    }
}
