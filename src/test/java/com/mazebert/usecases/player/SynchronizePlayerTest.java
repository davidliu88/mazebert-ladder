package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.mocks.FoilCardGatewayMock;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.gateways.mocks.PurchaseGatewayMock;
import com.mazebert.gateways.mocks.QuestGatewayMock;
import com.mazebert.plugins.random.mocks.RandomNumberGeneratorMock;
import com.mazebert.plugins.time.mocks.CurrentDatePluginMock;
import com.mazebert.usecases.player.SynchronizePlayer.Request;
import com.mazebert.usecases.player.SynchronizePlayer.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.util.Date;
import java.util.List;

import static com.mazebert.builders.BuilderFactory.*;
import static org.junit.Assert.*;
import static org.jusecase.builders.BuilderFactory.*;

public class SynchronizePlayerTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private FoilCardGatewayMock foilCardGateway = new FoilCardGatewayMock();
    private QuestGatewayMock questGateway = new QuestGatewayMock();
    private PurchaseGatewayMock productGateway = new PurchaseGatewayMock();
    private CurrentDatePluginMock currentDatePlugin = new CurrentDatePluginMock();
    private RandomNumberGeneratorMock randomNumberGenerator = new RandomNumberGeneratorMock();

    @Before
    public void setUp() {
        usecase = new SynchronizePlayer(playerGateway, foilCardGateway, questGateway,
                productGateway, currentDatePlugin, randomNumberGenerator);

        givenRequest(a(request()));
        playerGateway.givenPlayer(a(player().casid()));
    }

    @Test
    public void nullKey() {
        givenRequest(a(request().withKey(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Player key must not be null"));
    }

    @Test
    public void playerDoesNotExist() {
        playerGateway.givenNoPlayerExists();
        whenRequestIsExecuted();
        thenErrorIs(new NotFound("Player does not exist"));
    }

    @Test
    public void playerExists_propertiesAreFilled() {
        Player expected = a(player().casid());
        givenRequest(a(request()));
        playerGateway.givenPlayer(expected);

        whenRequestIsExecuted();

        assertEquals(expected.getId(), response.id);
        assertEquals(expected.getName(), response.name);
        assertEquals(expected.getLevel(), response.level);
        assertEquals(expected.getExperience(), response.experience);
        assertEquals(expected.getRelics(), response.relics);
    }

    @Test
    public void foilTowerCardsAreAdded() {
        foilCardGateway.givenFoilCardsForPlayer(a(player().casid()), a(listWith(
                a(foilCard().tower().withCardId(7).withAmount(5)),
                a(foilCard().tower().withCardId(10).withAmount(3))
        )));

        whenRequestIsExecuted();

        thenFoilCardsAre(a(listWith(
                a(card().withId(7).withAmount(5)),
                a(card().withId(10).withAmount(3))
        )), response.foilTowers);
    }

    @Test
    public void foilItemCardsAreAdded() {
        foilCardGateway.givenFoilCardsForPlayer(a(player().casid()), a(listWith(
                a(foilCard().item().withCardId(7).withAmount(5)),
                a(foilCard().item().withCardId(100).withAmount(1))
        )));

        whenRequestIsExecuted();

        thenFoilCardsAre(a(listWith(
                a(card().withId(7).withAmount(5)),
                a(card().withId(100).withAmount(1))
        )), response.foilItems);
    }

    @Test
    public void foilPotionCardsAreAdded() {
        foilCardGateway.givenFoilCardsForPlayer(a(player().casid()), a(listWith(
                a(foilCard().potion().withCardId(7).withAmount(5)),
                a(foilCard().potion().withCardId(100).withAmount(1))
        )));

        whenRequestIsExecuted();

        thenFoilCardsAre(a(listWith(
                a(card().withId(7).withAmount(5)),
                a(card().withId(100).withAmount(1))
        )), response.foilPotions);
    }

    @Test
    public void foilHeroCardsAreAdded() {
        foilCardGateway.givenFoilCardsForPlayer(a(player().casid()), a(listWith(
                a(foilCard().hero().withCardId(7).withAmount(5)),
                a(foilCard().hero().withCardId(100).withAmount(1))
        )));

        whenRequestIsExecuted();

        thenFoilCardsAre(a(listWith(
                a(card().withId(7).withAmount(5)),
                a(card().withId(100).withAmount(1))
        )), response.foilHeroes);
    }

    @Test
    public void completedHiddenQuestIdsAreAdded() {
        List<Long> expected = a(listWith(10L, 11L, 12L));
        questGateway.givenCompletedHiddenQuestIdsForPlayer(a(player().casid()), expected);

        whenRequestIsExecuted();

        assertEquals(expected, response.completedHiddenQuestIds);
    }

    @Test
    public void noDailyQuests() {
        questGateway.givenDailyQuestsForPlayer(a(player().casid()), a(listWith()));

        whenRequestIsExecuted();

        assertEquals(0, response.dailyQuests.size());
    }

    @Test
    public void dailyQuests() {
        List<Quest> expected = a(listWith(
                a(quest()),
                a(quest())
        ));
        questGateway.givenDailyQuestsForPlayer(a(player().casid()), expected);

        whenRequestIsExecuted();

        assertEquals(expected, response.dailyQuests);
    }

    @Test
    public void dailyQuestIsGenerated() {
        questGateway.givenQuests(a(listWith(
                a(quest().withId(10))
        )));
        questGateway.givenDailyQuestsForPlayer(a(player().casid()), a(listWith(
                a(quest().withId(1)),
                a(quest().withId(2))
        )));

        whenRequestIsExecuted();

        assertEquals(3, response.dailyQuests.size());
        assertEquals(10, response.dailyQuests.get(2).getId());
    }

    @Test
    public void timeZoneIsRespectedForDailyQuestCreation() {
        questGateway.givenQuests(a(listWith(
                a(quest().withId(10))
        )));
        questGateway.givenDailyQuestsForPlayer(a(player().casid()), a(listWith(
                a(quest().withId(1)),
                a(quest().withId(2))
        )));
        playerGateway.givenPlayer(a(player().casid()
                .withLastQuestCreation(a(date().with("2016-02-02 15:00:00"))))
        );
        givenRequest(a(request().golden().withTimeZoneOffset(+1))); // Actually -1: Adobe AIR App sends inverted Timezone.
        currentDatePlugin.givenCurrentDate(a(date().with("2016-02-03 00:00:01")));

        whenRequestIsExecuted();

        assertEquals(2, response.dailyQuests.size());
    }

    @Test
    public void questReplacementPossible() {
        givenPlayerHasQuestsCreatedAt(a(date().with("2016-01-01 16:00:00")));
        currentDatePlugin.givenCurrentDate(a(date().with("2016-01-03 16:00:00")));

        whenRequestIsExecuted();

        assertTrue(response.canReplaceDailyQuest);
    }

    @Test
    public void questReplacementImpossible() {
        givenPlayerHasQuestsCreatedAt(a(date().with("2016-01-01 16:00:00")));
        currentDatePlugin.givenCurrentDate(a(date().with("2016-01-01 16:00:00")));

        whenRequestIsExecuted();

        assertFalse(response.canReplaceDailyQuest);
    }

    @Test
    public void questReplacementImpossible_notEnoughQuests() {
        questGateway.givenDailyQuestsForPlayer(a(player().casid()), a(listWith(
                a(quest().withId(1)),
                a(quest().withId(2))
        )));
        playerGateway.givenPlayer(a(player().casid()
                .withLastQuestCreation(a(date().with("2016-01-01 16:00:00")))
        ));
        currentDatePlugin.givenCurrentDate(a(date().with("2016-01-03 16:00:00")));

        whenRequestIsExecuted();

        assertFalse(response.canReplaceDailyQuest);
    }

    @Test
    public void purchasedProductsAreAdded() {
        List<String> expected = a(listWith("com.mazebert.Cookie", "com.mazebert.Beer"));
        productGateway.givenPurchasedProductIds(a(player().casid()), expected);

        whenRequestIsExecuted();

        assertEquals(expected, response.purchasedProductIds);
    }

    @Test
    public void blackMarket_isAvailable() {
        currentDatePlugin.givenCurrentDate(a(date().with("2016-02-13 10:00:00")));
        whenRequestIsExecuted();
        assertTrue(response.isBlackMarketAvailable);
    }

    @Test
    public void blackMarket_isNotAvailable() {
        currentDatePlugin.givenCurrentDate(a(date().with("2016-02-15 10:00:00")));
        whenRequestIsExecuted();
        assertFalse(response.isBlackMarketAvailable);
    }

    private void thenFoilCardsAre(List<Response.Card> expected, List<Response.Card> actual) {
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); ++i) {
            thenFoilCardsAreEqual(expected.get(i), actual.get(i));
        }
    }

    private void thenFoilCardsAreEqual(Response.Card expected, Response.Card actual) {
        assertEquals(expected.id, actual.id);
        assertEquals(expected.amount, actual.amount);
    }

    private void givenPlayerHasQuestsCreatedAt(Date date) {
        questGateway.givenDailyQuestsForPlayer(a(player().casid()), a(listWith(
                a(quest().withId(1)),
                a(quest().withId(2)),
                a(quest().withId(3))
        )));
        playerGateway.givenPlayer(a(player().casid()
                .withLastQuestCreation(date)
        ));
    }

    private RequestBuilder request() {
        return new RequestBuilder().golden();
    }

    private ResponseCardBuilder card() {
        return new ResponseCardBuilder();
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        public RequestBuilder golden() {
            return this
                    .withKey("abcdef");
        }

        public Request build() {
            return request;
        }

        public RequestBuilder withKey(String value) {
            request.key = value;
            return this;
        }

        public RequestBuilder withTimeZoneOffset(int value) {
            request.timeZoneOffset = value;
            return this;
        }
    }

    private static class ResponseCardBuilder implements Builder<Response.Card> {
        private Response.Card card = new Response.Card();

        public ResponseCardBuilder withId(long value) {
            card.id = value;
            return this;
        }

        public ResponseCardBuilder withAmount(int value) {
            card.amount = value;
            return this;
        }

        @Override
        public Response.Card build() {
            return card;
        }
    }
}