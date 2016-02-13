package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.mocks.PlayerGatewayMock;
import com.mazebert.plugins.time.mocks.CurrentDatePluginMock;
import com.mazebert.usecases.player.UpdateProgress.Request;
import com.mazebert.usecases.player.UpdateProgress.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.date;

public class UpdateProgressTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayMock playerGateway = new PlayerGatewayMock();
    private CurrentDatePluginMock currentDatePlugin = new CurrentDatePluginMock();

    @Before
    public void setUp() {
        usecase = new UpdateProgress(playerGateway, currentDatePlugin);
    }

    @Test
    public void nullKey() {
        givenRequest(a(request().withKey(null)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Player key must not be null."));
    }

    @Test
    public void playerDoesNotExist() {
        givenRequest(a(request().withKey("unknown")));
        whenRequestIsExecuted();
        thenErrorIs(new NotFound("A player with this key does not exist."));
    }

    @Test
    public void playerValuesAreUpdated() {
        givenRequest(a(request()
                .withLevel(100)
                .withExperience(100000)));
        playerGateway.givenPlayer(a(player().casid()));

        whenRequestIsExecuted();

        Player player = playerGateway.getUpdatedPlayer();
        assertEquals(100, player.getLevel());
        assertEquals(100000, player.getExperience());
    }

    @Test
    public void lastUpdateIsAdjusted() {
        givenRequest(a(request()));
        playerGateway.givenPlayer(a(player().casid()));
        currentDatePlugin.givenCurrentDate(a(date().with("2018-10-10 22:00:00")));

        whenRequestIsExecuted();

        assertEquals(a(date().with("2018-10-10 22:00:00")), playerGateway.getUpdatedPlayer().getLastUpdate());
    }

    @Test
    public void responseIsNotNull() {
        givenRequest(a(request()));
        playerGateway.givenPlayer(a(player().casid()));

        whenRequestIsExecuted();

        thenResponseIsNotNull();
    }

    @Test
    public void experienceCannotBeReduced() {
        givenRequest(a(request().withExperience(1000)));
        playerGateway.givenPlayer(a(player().casid().withExperience(1001)));

        whenRequestIsExecuted();

        assertEquals(1001, playerGateway.getUpdatedPlayer().getExperience());
    }

    @Test
    public void levelCannotBeReduced() {
        givenRequest(a(request().withLevel(10)));
        playerGateway.givenPlayer(a(player().casid().withLevel(20)));

        whenRequestIsExecuted();

        assertEquals(20, playerGateway.getUpdatedPlayer().getLevel());
    }

    private RequestBuilder request() {
        return new RequestBuilder().golden();
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        public RequestBuilder golden() {
            return this
                    .withKey("abcdef")
                    .withLevel(10)
                    .withExperience(1000);
        }

        public RequestBuilder withKey(String value) {
            request.key = value;
            return this;
        }

        public RequestBuilder withLevel(int value) {
            request.level = value;
            return this;
        }

        public RequestBuilder withExperience(long value) {
            request.experience = value;
            return this;
        }

        public Request build() {
            return request;
        }
    }
}