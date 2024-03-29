package com.mazebert.gateways;

import com.mazebert.entities.CardType;
import com.mazebert.entities.FoilCard;
import org.junit.Test;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.foilCard;
import static org.junit.Assert.*;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.list;

public abstract class FoilCardGatewayTest extends GatewayTest<FoilCardGateway> {


    @Test
    public void getFoilCardsForPlayerId_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.getFoilCardsForPlayerId(1));
        thenGatewayErrorIs("Failed to get foil cards for player.");
    }

    @Test
    public void getFoilCardsForPlayerId_noFoilCards() {
        assertEquals(a(list()), gateway.getFoilCardsForPlayerId(1));
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

    @Test
    public void addFoilCardToPlayer_continuousAddition() {
        gateway.addFoilCardToPlayer(1, a(foilCard().bowlingBall().withAmount(3)));
        assertEquals(3, gateway.getFoilCardAmount(1, 58, CardType.ITEM));

        gateway.addFoilCardToPlayer(1, a(foilCard().bowlingBall().withAmount(1)));
        assertEquals(4, gateway.getFoilCardAmount(1, 58, CardType.ITEM));

        gateway.addFoilCardToPlayer(1, a(foilCard().bowlingBall().withAmount(10)));
        assertEquals(14, gateway.getFoilCardAmount(1, 58, CardType.ITEM));
    }

    @Test
    public void getFoilCardAmount_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.getFoilCardAmount(1, 1, CardType.TOWER));
        thenGatewayErrorIs("Failed to get foil card amount.");
    }

    @Test
    public void getFoilCardAmount_noEntry() {
        assertEquals(0, gateway.getFoilCardAmount(1, 1, CardType.TOWER));
    }

    @Test
    public void getFoilCardAmount_correctAmountReturned() {
        gateway.addFoilCardToPlayer(1, a(foilCard().withCardId(10).withCardType(CardType.ITEM).withAmount(8)));
        gateway.addFoilCardToPlayer(1, a(foilCard().withCardId(20).withCardType(CardType.ITEM).withAmount(1)));
        gateway.addFoilCardToPlayer(1, a(foilCard().withCardId(10).withCardType(CardType.TOWER).withAmount(4)));

        assertEquals(8, gateway.getFoilCardAmount(1, 10, CardType.ITEM));
    }

    @Test
    public void setAmountOfAllPlayerFoilCards_gatewayError() {
        whenGatewayErrorIsForced(() -> errorGateway.setAmountOfAllPlayerFoilCards(1, 100));
        thenGatewayErrorIs("Failed to set amount of all player foil cards.");
    }

    @Test
    public void setAmountOfAllPlayerFoilCards_amountIsChanged() {
        gateway.addFoilCardToPlayer(1, a(foilCard().withCardId(10).withCardType(CardType.ITEM).withAmount(8)));
        gateway.addFoilCardToPlayer(1, a(foilCard().withCardId(20).withCardType(CardType.ITEM).withAmount(1)));
        gateway.addFoilCardToPlayer(1, a(foilCard().withCardId(10).withCardType(CardType.TOWER).withAmount(4)));
        gateway.addFoilCardToPlayer(2, a(foilCard().withCardId(10).withCardType(CardType.TOWER).withAmount(4)));

        gateway.setAmountOfAllPlayerFoilCards(1, 1);

        assertEquals(1, gateway.getFoilCardAmount(1, 10, CardType.ITEM));
        assertEquals(1, gateway.getFoilCardAmount(1, 20, CardType.ITEM));
        assertEquals(1, gateway.getFoilCardAmount(1, 10, CardType.TOWER));
        assertEquals("Other players are not affected", 4, gateway.getFoilCardAmount(2, 10, CardType.TOWER));
    }
}
