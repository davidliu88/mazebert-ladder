package com.mazebert.usecases.player;

import com.mazebert.entities.Card;
import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.error.ServiceUnavailable;
import com.mazebert.gateways.mocks.CardGatewayMock;
import com.mazebert.gateways.mocks.FoilCardGatewayMock;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.gateways.transaction.mocks.TransactionRunnerMock;
import com.mazebert.usecases.player.AbstractBuyCard.Response;
import com.mazebert.usecases.player.ForgeCard.Request;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.item;
import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;

public class ForgeCardTest extends UsecaseTest<Request, Response> {
    private TransactionRunnerMock transactionRunner = new TransactionRunnerMock();
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private FoilCardGatewayMock foilCardGateway = new FoilCardGatewayMock();
    private CardGatewayMock cardGateway = new CardGatewayMock();

    private Player player = a(player().casid());

    @Before
    public void setUp() {
        usecase = new ForgeCard(transactionRunner, foilCardGateway, playerGateway, cardGateway);

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
        givenRequest(a(request().withAppVersion("0.9.1")));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("At least app version 1.0.0 is required for this request."));
    }

    @Test
    public void nullKey() {
        givenRequest(a(request().withKey(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Key must not be null."));
    }

    @Test
    public void playerNotFound() {
        givenRequest(a(request().withKey("unknown")));
        whenRequestIsExecuted();
        thenErrorIs(new NotFound("Player does not exist."));
    }

    @Test
    public void notEnoughRelics() {
        playerGateway.givenPlayerExists(a(player().casid().withRelics(19)));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenErrorIs(new ServiceUnavailable("Come back later when you have enough relics."));
    }

    @Test
    public void noCardsExist() {
        givenRequest(a(request()));
        whenRequestIsExecuted();
        thenErrorIs(new ServiceUnavailable("There are no cards available that can be forged."));
    }

    @Test
    public void purchasedCardIsReturned() {
        cardGateway.givenCardExists(a(item().babySword()));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenCardIsReturned(a(item().babySword()));
    }

    @Test
    public void cardIsPurchased() {
        cardGateway.givenCardExists(a(item().babySword()));
        playerGateway.givenPlayerExists(a(player().casid().withRelics(300)));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenPlayerRelicsAre(280);
        foilCardGateway.thenFoilCardWasAddedToPlayer(player, a(item().babySword()));
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
                .withAppVersion("1.0.0")
                .withKey("abcdef");
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
    }
}