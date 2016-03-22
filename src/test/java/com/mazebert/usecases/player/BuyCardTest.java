package com.mazebert.usecases.player;

import com.mazebert.entities.Card;
import com.mazebert.entities.CardType;
import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.error.ServiceUnavailable;
import com.mazebert.gateways.mocks.CardGatewayMock;
import com.mazebert.gateways.mocks.FoilCardGatewayMock;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.gateways.transaction.mocks.TransactionRunnerMock;
import com.mazebert.usecases.player.BuyCard.Request;
import com.mazebert.usecases.player.AbstractBuyCard.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.player;
import static com.mazebert.builders.BuilderFactory.tower;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;

public class BuyCardTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private FoilCardGatewayMock foilCardGateway = new FoilCardGatewayMock();
    private CardGatewayMock cardGateway = new CardGatewayMock();
    private TransactionRunnerMock transactionRunner = new TransactionRunnerMock();

    private Player player = a(player().casid());

    @Before
    public void setUp() {
        usecase = new BuyCard(playerGateway, foilCardGateway, cardGateway, transactionRunner);

        playerGateway.givenPlayerExists(player);
    }

    @Test
    public void nullAppVersion() {
        givenRequest(a(request().withAppVersion(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("App version must not be null."));
    }

    @Test
    public void tooLowAppVersion() {
        givenRequest(a(request().withAppVersion("1.3.1")));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("At least app version 1.4.0 is required for this request."));
    }

    @Test
    public void invalidCardType() {
        givenRequest(a(request().withCardType(0)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("This card type does not exist."));
    }

    @Test
    public void cardDoesNotExist() {
        givenRequest(a(request().withCardId(0)));
        whenRequestIsExecuted();
        thenErrorIs(new NotFound("The requested card does not exist."));
    }

    @Test
    public void notEnoughRelics() {
        cardGateway.givenCardExists(a(tower().huliTheMonkey()));
        playerGateway.givenPlayerExists(a(player().casid().withRelics(100)));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenErrorIs(new ServiceUnavailable("Come back later when you have enough relics."));
    }

    @Test
    public void cardIsPurchased() {
        cardGateway.givenCardExists(a(tower().huliTheMonkey()));
        playerGateway.givenPlayerExists(a(player().casid().withRelics(1000)));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenPlayerRelicsAre(850);
        thenCardIsReturned(a(tower().huliTheMonkey()));
    }

    private void thenCardIsReturned(Card card) {
        assertEquals(response.id, card.getId());
        assertEquals(response.type, card.getType());
    }

    private void thenPlayerRelicsAre(int relics) {
        assertEquals(relics, playerGateway.getRelics(player.getId()));
        assertEquals(relics, response.relics);
    }

    private RequestBuilder request() {
        return new RequestBuilder()
                .withAppVersion("1.4.0")
                .withKey("abcdef")
                .withCardId(7)
                .withCardType(CardType.TOWER);
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        @Override
        public Request build() {
            return request;
        }

        public RequestBuilder withAppVersion(String value) {
            request.appVersion = value;
            return this;
        }

        public RequestBuilder withKey(String value) {
            request.key = value;
            return this;
        }

        public RequestBuilder withCardId(long value) {
            request.cardId = value;
            return this;
        }

        public RequestBuilder withCardType(int value) {
            request.cardType = value;
            return this;
        }
    }
}