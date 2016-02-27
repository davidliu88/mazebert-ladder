package com.mazebert.usecases.quest;

import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.mocks.FoilCardGatewayMock;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.gateways.mocks.QuestGatewayMock;
import com.mazebert.plugins.random.mocks.RandomNumberGeneratorMock;
import com.mazebert.plugins.time.mocks.CurrentDatePluginMock;
import com.mazebert.usecases.quest.ReplaceQuest.Request;
import com.mazebert.usecases.quest.ReplaceQuest.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.player;
import static com.mazebert.builders.BuilderFactory.quest;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.list;

public class ReplaceQuestTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private QuestGatewayMock questGateway = new QuestGatewayMock();
    private FoilCardGatewayMock foilCardGateway = new FoilCardGatewayMock();
    private CurrentDatePluginMock currentDatePlugin = new CurrentDatePluginMock();
    private RandomNumberGeneratorMock randomNumberGenerator = new RandomNumberGeneratorMock();

    private Player player = a(player().casid());

    @Before
    public void setUp() {
        usecase = new ReplaceQuest(playerGateway,
                questGateway,
                foilCardGateway,
                currentDatePlugin,
                randomNumberGenerator);

        playerGateway.givenPlayerExists(player);
    }

    @Test
    public void nullAppVersion() {
        givenRequest(a(request().withAppVersion(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("App version must not be null."));
    }

    @Test
    public void tooOldAppVersion() {
        givenRequest(a(request().withAppVersion("0.9.0")));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("At least app version 1.0.0 is required for this request."));
    }

    @Test
    public void nullKey() {
        givenRequest(a(request().withKey(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Player key must not be null."));
    }

    @Test
    public void playerDoesNotExist() {
        givenRequest(a(request().withKey("123456")));
        whenRequestIsExecuted();
        thenErrorIs(new NotFound("A player with this key does not exist."));
    }

    @Test
    public void invalidQuestId() {
        questGateway.givenDailyQuestsForPlayer(player, a(list(
                a(quest().rollStrikesWithBowlingBall())
        )));
        givenRequest(a(request().withQuestId(9999L)));

        whenRequestIsExecuted();

        thenErrorIs(new NotFound("The player currently has no daily quest with this ID."));
    }

    @Test
    public void questReplacementNotPossible() {
        Quest expected = a(quest().rollStrikesWithBowlingBall().withId(50L));
        questGateway.givenQuests(a(list(expected)));
        questGateway.givenDailyQuestsForPlayer(player, a(list(
                a(quest().rollStrikesWithBowlingBall().withId(10L)),
                a(quest().rollStrikesWithBowlingBall().withId(11L))
        )));
        givenRequest(a(request().withQuestId(11L)));

        whenRequestIsExecuted();

        assertEquals(null, response.quest);
    }

    @Test
    public void questReplacementPossible() {
        Quest expected = a(quest().rollStrikesWithBowlingBall().withId(50L));
        questGateway.givenQuests(a(list(
                expected
        )));
        questGateway.givenDailyQuestsForPlayer(player, a(list(
                a(quest().rollStrikesWithBowlingBall().withId(10L)),
                a(quest().rollStrikesWithBowlingBall().withId(11L)),
                a(quest().rollStrikesWithBowlingBall().withId(12L))
        )));
        givenRequest(a(request().withQuestId(11L)));

        whenRequestIsExecuted();

        assertEquals(a(list(10L, 50L, 12L)), questGateway.findDailyQuestIds(player.getId()));
        assertEquals(expected, response.quest);
    }

    private RequestBuilder request() {
        return new RequestBuilder()
                .withAppVersion("1.2.0")
                .withKey("abcdef")
                .withQuestId(1L)
                .withTimeZoneOffset(0);
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

        public RequestBuilder withQuestId(long value) {
            request.questId = value;
            return this;
        }

        public RequestBuilder withTimeZoneOffset(int value) {
            request.timeZoneOffset = value;
            return this;
        }

        public RequestBuilder withKey(String value) {
            request.key = value;
            return this;
        }
    }
}