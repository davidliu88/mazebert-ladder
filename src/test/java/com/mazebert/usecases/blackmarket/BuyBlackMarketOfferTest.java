package com.mazebert.usecases.blackmarket;

import com.mazebert.entities.BlackMarketOffer;
import com.mazebert.entities.CardType;
import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.error.ServiceUnavailable;
import com.mazebert.gateways.mocks.*;
import org.jusecase.transaction.simple.mocks.TransactionRunnerMock;
import com.mazebert.plugins.random.mocks.RandomNumberGeneratorMock;
import com.mazebert.plugins.time.mocks.CurrentDatePluginMock;
import com.mazebert.usecases.blackmarket.BuyBlackMarketOffer.Request;
import com.mazebert.usecases.player.AbstractBuyCard.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.*;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.list;

public class BuyBlackMarketOfferTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private BlackMarketOfferGatewayMock blackMarketOfferGateway = new BlackMarketOfferGatewayMock();
    private BlackMarketSettingsGatewayMock blackMarketSettingsGateway = new BlackMarketSettingsGatewayMock();
    private CardGatewayMock cardGateway = new CardGatewayMock();
    private CurrentDatePluginMock currentDatePlugin = new CurrentDatePluginMock();
    private RandomNumberGeneratorMock randomNumberGenerator = new RandomNumberGeneratorMock();
    private TransactionRunnerMock transactionRunner = new TransactionRunnerMock();
    private FoilCardGatewayMock foilCardGateway = new FoilCardGatewayMock();

    private Player player = a(player().casid());

    @Before
    public void setUp() {
        usecase = new BuyBlackMarketOffer(playerGateway,
                blackMarketOfferGateway,
                currentDatePlugin,
                blackMarketSettingsGateway,
                cardGateway,
                randomNumberGenerator,
                transactionRunner, foilCardGateway);

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
        thenErrorIs(new BadRequest("At least app version 1.1.0 is required for this request."));
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
    public void blackMarketNotAvailable() {
        givenRequest(a(request()));
        whenRequestIsExecuted();
        thenErrorIs(new ServiceUnavailable("The black market is not available at the moment."));
    }

    @Test
    public void blackMarketOfferAlreadyPurchased() {
        BlackMarketOffer currentOffer = a(blackMarketOffer().withCard(item().bowlingBall()));
        blackMarketOfferGateway.givenLatestOffer(currentOffer);
        blackMarketOfferGateway.givenOfferIsPurchased(currentOffer, a(player().casid()));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenErrorIs(new ServiceUnavailable("This week's offer is already purchased."));
    }

    @Test
    public void notEnoughRelics() {
        BlackMarketOffer currentOffer = a(blackMarketOffer().withCard(item().bowlingBall()));
        blackMarketOfferGateway.givenLatestOffer(currentOffer);
        blackMarketSettingsGateway.givenSettings(a(blackMarketSettings().withPrice(200)));
        playerGateway.givenPlayerExists(a(player().casid().withRelics(199)));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenErrorIs(new ServiceUnavailable("Come back later when you have enough relics."));
    }

    @Test
    public void cardIsPurchased() {
        BlackMarketOffer currentOffer = a(blackMarketOffer().withCard(item().bowlingBall()));
        blackMarketOfferGateway.givenLatestOffer(currentOffer);
        blackMarketSettingsGateway.givenSettings(a(blackMarketSettings().withPrice(200)));
        playerGateway.givenPlayerExists(a(player().casid().withRelics(300)));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        thenPlayerRelicsAre(100);
        foilCardGateway.thenFoilCardWasAddedToPlayer(player, a(item().bowlingBall()));
        blackMarketOfferGateway.thenOfferIsPurchasedByPlayer(currentOffer, player);
        assertEquals(58, response.id);
        assertEquals(CardType.ITEM, response.type);
        assertEquals(1, response.amount);
    }

    @Test
    public void newFoilCardAmountIsReturned() {
        BlackMarketOffer currentOffer = a(blackMarketOffer().withCard(item().bowlingBall()));
        blackMarketOfferGateway.givenLatestOffer(currentOffer);
        foilCardGateway.givenFoilCardsForPlayer(player, a(list(
                a(foilCard().withCard(a(item().bowlingBall())).withAmount(10))
        )));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals(11, response.amount);
    }

    @Test
    public void kiwiEggSpecial() {
        BlackMarketOffer currentOffer = a(blackMarketOffer().withCard(tower().kiwiEgg()));
        blackMarketOfferGateway.givenLatestOffer(currentOffer);
        givenRequest(a(request().withAppVersion("1.3.0")));

        whenRequestIsExecuted();

        foilCardGateway.thenFoilCardWasAddedToPlayer(player, a(tower().kiwiEgg()));
        foilCardGateway.thenFoilCardWasAddedToPlayer(player, a(tower().kiwi()));
    }

    private void thenPlayerRelicsAre(int relics) {
        assertEquals(relics, playerGateway.getRelics(player.getId()));
        assertEquals(relics, response.relics);
    }

    private RequestBuilder request() {
        return new RequestBuilder()
                .withAppVersion("1.1.0")
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