package com.mazebert.usecases.quest;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.mocks.FoilCardGatewayMock;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.gateways.mocks.QuestGatewayMock;
import com.mazebert.usecases.quest.CompleteQuests.Request;
import com.mazebert.usecases.quest.CompleteQuests.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.*;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.list;

public class CompleteQuestsTest extends UsecaseTest<Request, Response> {

    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private QuestGatewayMock questGateway = new QuestGatewayMock();
    private FoilCardGatewayMock foilCardGateway = new FoilCardGatewayMock();

    @Before
    public void setUp() {
        usecase = new CompleteQuests(playerGateway, questGateway, foilCardGateway);
    }

    @Test
    public void appVersionTooOld() {
        givenRequest(a(request().withAppVersion("0.9.0")));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("This app version is too old to complete quests."));
    }

    @Test
    public void nullKey() {
        givenRequest(a(request().withKey(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Player key must be provided."));
    }

    @Test
    public void nullQuestIds() {
        givenRequest(a(request().withQuestTransactionIds(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Quest transaction IDs need to be provided."));
    }

    @Test
    public void playerDoesNotExist() {
        givenRequest(a(request()));
        whenRequestIsExecuted();
        thenErrorIs(new NotFound("No player with this key exists."));
    }

    @Test
    public void noCompletedQuests() {
        playerGateway.givenPlayerExists(a(player().casid().withRelics(0)));
        givenRequest(a(request().withQuestTransactionIds(a(list()))));

        whenRequestIsExecuted();

        thenPlayerRelicsAre(0);
    }

    @Test
    public void oneCompletedDailyQuest_doesNotExist() {
        Player player = a(player().casid().withRelics(0));
        playerGateway.givenPlayerExists(player);
        questGateway.givenQuests(a(list(
                a(quest().daily().withId(1L).withReward(60))
        )));
        givenRequest(a(request().withQuestTransactionIds(a(list(2L)))));

        whenRequestIsExecuted();

        thenPlayerRelicsAre(0);
    }

    @Test
    public void oneCompletedDailyQuest_notOwnedByPlayer() {
        Player player = a(player().casid().withRelics(0));
        playerGateway.givenPlayerExists(player);
        questGateway.givenQuests(a(list(
                a(quest().daily().withId(1L).withReward(60))
        )));
        questGateway.givenDailyQuestsForPlayer(player, a(list(
                a(quest().daily().withId(2L).withReward(60))
        )));
        givenRequest(a(request().withQuestTransactionIds(a(list(1L)))));

        whenRequestIsExecuted();

        thenPlayerRelicsAre(0);
    }

    @Test
    public void oneCompletedDailyQuest_ownedByPlayer() {
        Player player = a(player().casid().withRelics(0));
        playerGateway.givenPlayerExists(player);
        questGateway.givenQuests(a(list(
                a(quest().daily().withId(1L).withReward(60))
        )));
        questGateway.givenDailyQuestsForPlayer(player, a(list(
                a(quest().daily().withId(1L).withReward(60))
        )));
        givenRequest(a(request().withQuestTransactionIds(a(list(1L)))));

        whenRequestIsExecuted();

        thenPlayerRelicsAre(60);
    }

    @Test
    public void threeCompletedDailyQuest_ownedByPlayer() {
        Player player = a(player().casid().withRelics(0));
        playerGateway.givenPlayerExists(player);
        questGateway.givenQuests(a(list(
                a(quest().daily().withId(1L).withReward(60)),
                a(quest().daily().withId(2L).withReward(20)),
                a(quest().daily().withId(3L).withReward(40))
        )));
        questGateway.givenDailyQuestsForPlayer(player, a(list(
                a(quest().daily().withId(1L).withReward(60)),
                a(quest().daily().withId(2L).withReward(20)),
                a(quest().daily().withId(3L).withReward(40))
        )));
        givenRequest(a(request().withQuestTransactionIds(a(list(1L, 2L, 3L)))));

        whenRequestIsExecuted();

        thenPlayerRelicsAre(120);
    }

    @Test
    public void dailyQuestNoLongerOwnedByPlayerAfterCompletion() {
        Player player = a(player().casid().withRelics(0));
        playerGateway.givenPlayerExists(player);
        questGateway.givenQuests(a(list(
                a(quest().daily().withId(1L).withReward(60))
        )));
        questGateway.givenDailyQuestsForPlayer(player, a(list(
                a(quest().daily().withId(1L).withReward(60))
        )));
        givenRequest(a(request().withQuestTransactionIds(a(list(1L)))));

        whenRequestIsExecuted();

        assertEquals(0, questGateway.findDailyQuests(player.getId()).size());
    }

    @Test
    public void playerRelicsAreIncreased() {
        Player player = a(player().casid().withRelics(100));
        playerGateway.givenPlayerExists(player);
        questGateway.givenQuests(a(list(
                a(quest().daily().withId(1L).withReward(60))
        )));
        questGateway.givenDailyQuestsForPlayer(player, a(list(
                a(quest().daily().withId(1L).withReward(60))
        )));
        givenRequest(a(request().withQuestTransactionIds(a(list(1L)))));

        whenRequestIsExecuted();

        thenPlayerRelicsAre(160);
    }

    @Test
    public void hiddenQuest_alreadyCompleted() {
        Player player = a(player().casid().withRelics(0));
        playerGateway.givenPlayerExists(player);
        questGateway.givenQuests(a(list(
                a(quest().hidden().withId(1L).withReward(200))
        )));
        questGateway.givenCompletedHiddenQuestIdsForPlayer(player, a(list(
                1L
        )));
        givenRequest(a(request().withQuestTransactionIds(a(list(1L)))));

        whenRequestIsExecuted();

        thenPlayerRelicsAre(0);
    }

    @Test
    public void hiddenQuest_notYetCompleted() {
        Player player = a(player().casid().withRelics(0));
        playerGateway.givenPlayerExists(player);
        questGateway.givenQuests(a(list(
                a(quest().hidden().withId(1L).withReward(200))
        )));
        questGateway.givenCompletedHiddenQuestIdsForPlayer(player, a(list(
                2L
        )));
        givenRequest(a(request().withQuestTransactionIds(a(list(1L)))));

        whenRequestIsExecuted();

        thenPlayerRelicsAre(200);
    }

    @Test
    public void hiddenQuest_markedAsComplete() {
        Player player = a(player().casid().withRelics(0));
        playerGateway.givenPlayerExists(player);
        questGateway.givenQuests(a(list(
                a(quest().hidden().withId(1L).withReward(200))
        )));
        questGateway.givenCompletedHiddenQuestIdsForPlayer(player, a(list(
                2L
        )));
        givenRequest(a(request().withQuestTransactionIds(a(list(1L)))));

        whenRequestIsExecuted();

        assertEquals(a(list(2L, 1L)), questGateway.findCompletedHiddenQuestIds(player.getId()));
    }

    @Test
    public void playerUnlocksGoldenGrounds_receivesGoldenScepterOfTime() {
        Player player = a(player().casid());
        Quest quest = a(quest().completeTwistedPaths());
        playerGateway.givenPlayerExists(player);
        questGateway.givenQuests(a(list(quest)));
        questGateway.givenCompletedHiddenQuestIdsForPlayer(player, a(list()));
        givenRequest(a(request().withQuestTransactionIds(a(list(
                quest.getId()
        )))));

        whenRequestIsExecuted();

        foilCardGateway.thenFoilCardWasAddedToPlayer(player, a(item().scepterOfTime()));
    }

    @Test
    public void playerCompletesNormalHiddenQuest_noFoilCardsAreAdded() {
        Player player = a(player().casid());
        Quest quest = a(quest().hidden().withId(1000L));
        playerGateway.givenPlayerExists(player);
        questGateway.givenQuests(a(list(quest)));
        questGateway.givenCompletedHiddenQuestIdsForPlayer(player, a(list()));
        givenRequest(a(request().withQuestTransactionIds(a(list(
                quest.getId()
        )))));

        whenRequestIsExecuted();

        foilCardGateway.thenNoFoilCardsWereAddedToPlayer(player);
    }

    private void thenPlayerRelicsAre(int relics) {
        assertEquals(relics, response.relics);
    }

    private RequestBuilder request() {
        return new RequestBuilder()
                .withAppVersion("1.0.0")
                .withKey("abcdef")
                .withQuestTransactionIds(a(list(1L)));
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

        public RequestBuilder withQuestTransactionIds(List<Long> value) {
            request.questTransactionIds = value;
            return this;
        }
    }
}