package com.mazebert.usecases.player;

import com.mazebert.entities.CardRarity;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.mocks.CardGatewayMock;
import com.mazebert.gateways.mocks.FoilCardGatewayMock;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.usecases.player.TradeDuplicateCards.OfferPart;
import com.mazebert.usecases.player.TradeDuplicateCards.Request;
import com.mazebert.usecases.player.TradeDuplicateCards.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.*;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.list;

public class TradeDuplicateCardsTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private CardGatewayMock cardGateway = new CardGatewayMock();
    private FoilCardGatewayMock foilCardGateway = new FoilCardGatewayMock();

    @Before
    public void setUp() {
        usecase = new TradeDuplicateCards(playerGateway, cardGateway, foilCardGateway);
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
    public void offer_noFoilCards() {
        playerGateway.givenPlayerExists(a(player().casid()));
        givenRequest(a(request().offer()));

        whenRequestIsExecuted();

        thenEmptyOfferIsReturned();
    }

    @Test
    public void offer_noDuplicateFoilCards() {
        playerGateway.givenPlayerExists(a(player().casid()));
        foilCardGateway.givenFoilCardsForPlayer(a(player().casid()), a(list(
                a(foilCard().withCard(a(tower().huliTheMonkey())).withAmount(1)),
                a(foilCard().withCard(a(item().babySword())).withAmount(1)),
                a(foilCard().withCard(a(potion().tearsOfTheGods())).withAmount(1)),
                a(foilCard().withCard(a(hero().littlefinger())).withAmount(1))
        )));
        givenRequest(a(request().offer()));

        whenRequestIsExecuted();

        thenEmptyOfferIsReturned();
    }

    @Test
    public void offer_cardDoesNotExist() {
        playerGateway.givenPlayerExists(a(player().casid()));
        foilCardGateway.givenFoilCardsForPlayer(a(player().casid()), a(list(
                a(foilCard().withCard(a(hero().littlefinger())).withAmount(2))
        )));
        givenRequest(a(request().offer()));

        whenRequestIsExecuted();

        thenEmptyOfferIsReturned();
    }

    @Test
    public void offer_duplicateFoilCard() {
        playerGateway.givenPlayerExists(a(player().casid()));
        cardGateway.givenCardsExist(a(list(
                a(tower().huliTheMonkey()),
                a(tower().herbWitch()),
                a(item().babySword()),
                a(item().mjoelnir()),
                a(potion().tearsOfTheGods()),
                a(hero().littlefinger())
        )));
        foilCardGateway.givenFoilCardsForPlayer(a(player().casid()), a(list(
                a(foilCard().withCard(a(tower().huliTheMonkey())).withAmount(11)),
                a(foilCard().withCard(a(tower().herbWitch())).withAmount(3)),
                a(foilCard().withCard(a(item().babySword())).withAmount(2)),
                a(foilCard().withCard(a(item().mjoelnir())).withAmount(2)),
                a(foilCard().withCard(a(potion().tearsOfTheGods())).withAmount(2)),
                a(foilCard().withCard(a(hero().littlefinger())).withAmount(1))
        )));
        givenRequest(a(request().offer()));

        whenRequestIsExecuted();

        thenOfferTotalIs(2 + 8 + 80 + 16 + 40);
        thenOfferForRarityIs(CardRarity.COMMON, 1, 2);
        thenOfferForRarityIs(CardRarity.UNCOMMON, 2, 8);
        thenOfferForRarityIs(CardRarity.RARE, 10, 80);
        thenOfferForRarityIs(CardRarity.UNIQUE, 1, 16);
        thenOfferForRarityIs(CardRarity.LEGENDARY, 1, 40);
    }

    private void thenOfferTotalIs(int expected) {
        assertEquals(expected, response.offer.total);
    }

    private void thenOfferForRarityIs(int rarity, int amount, int reward) {
        OfferPart part = response.offer.rarities[rarity - 1];
        assertEquals(amount, part.amount);
        assertEquals(reward, part.reward);
    }

    private void thenEmptyOfferIsReturned() {
        assertEquals(0, response.offer.total);

        OfferPart[] rarities = response.offer.rarities;
        for (int i = 0; i < CardRarity.COUNT; ++i) {
            assertEquals(0, rarities[i].amount);
            assertEquals(0, rarities[i].reward);
        }
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

        public RequestBuilder offer() {
            request.isOffer = true;
            return this;
        }
    }
}