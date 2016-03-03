package com.mazebert.usecases.player;

import com.mazebert.error.NotFound;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.plugins.time.mocks.CurrentDatePluginMock;
import com.mazebert.usecases.player.GetPlayerProfile.Request;
import com.mazebert.usecases.player.GetPlayerProfile.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.date;

public class GetPlayerProfileTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private CurrentDatePluginMock currentDatePlugin = new CurrentDatePluginMock();

    @Before
    public void setUp() {
        usecase = new GetPlayerProfile(playerGateway, currentDatePlugin);

        playerGateway.givenPlayerExists(a(player().casid()));
    }

    @Test
    public void playerDoesNotExist() {
        givenRequest(a(request().withId(10)));
        whenRequestIsExecuted();
        thenErrorIs(new NotFound("A player with id '10' does not exist."));
    }

    @Test
    public void playerInformationIsAddedToResponse() {
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("casid", response.name);
        assertEquals(99, response.level);
        assertEquals(99999, response.experience);
        assertEquals(7, response.supporterLevel);
        assertEquals(300, response.relics);
    }

    @Test
    public void playerRankIsAddedToResponse() {
        playerGateway.givenPlayerRank(700, a(player().casid()));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals(700, response.rank);
    }

    @Test
    public void lastUpdateAtTimeOfRequest() {
        currentDatePlugin.givenCurrentDate(a(date().with("2016-01-21 19:21:12")));
        playerGateway.givenPlayerExists(a(player().casid().withLastUpdate(a(date().with("2016-01-21 19:21:12")))));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("Now playing", response.lastPlayed);
    }

    @Test
    public void lastUpdate12MinutesAgo() {
        currentDatePlugin.givenCurrentDate(a(date().with("2016-01-21 19:33:12")));
        playerGateway.givenPlayerExists(a(player().casid().withLastUpdate(a(date().with("2016-01-21 19:21:12")))));
        givenRequest(a(request()));

        whenRequestIsExecuted();

        assertEquals("Last seen 12 minutes ago", response.lastPlayed);
    }

    @Test
    public void emptyFoilHeroes() {
        // TODO implement me!
    }

    private RequestBuilder request() {
        return new RequestBuilder().withId(115);
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        @Override
        public Request build() {
            return request;
        }

        public RequestBuilder withId(long value) {
            request.id = value;
            return this;
        }
    }
}