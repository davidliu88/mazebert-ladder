package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.FoilCardGatewayCoach;
import com.mazebert.gateways.PlayerGatewayCoach;
import com.mazebert.gateways.QuestGatewayCoach;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;
import com.mazebert.usecases.player.SynchronizePlayer.Request;
import com.mazebert.usecases.player.SynchronizePlayer.Response;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.foilCard;
import static com.mazebert.builders.BuilderFactory.player;
import static com.mazebert.builders.BuilderFactory.quest;
import static org.junit.Assert.assertEquals;
import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.listWith;

public class SynchronizePlayerTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayCoach playerGateway;
    private FoilCardGatewayCoach foilCardGateway;
    private QuestGatewayCoach questGateway;

    @Before
    public void setUp() {
        playerGateway = new PlayerGatewayCoach();
        foilCardGateway = new FoilCardGatewayCoach();
        questGateway = new QuestGatewayCoach();
        usecase = new SynchronizePlayer(playerGateway, foilCardGateway, questGateway);

        givenRequest(a(request()));
        playerGateway.givenPlayer(a(player().casid()));
    }

    @Test
    public void nullKey() {
        givenRequest(a(request().withKey(null)));
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.BAD_REQUEST, "Player key must not be null"));
    }

    @Test
    public void playerDoesNotExist() {
        playerGateway.givenNoPlayerExists();
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.NOT_FOUND, "Player does not exist"));
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