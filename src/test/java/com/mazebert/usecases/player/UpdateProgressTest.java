package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.PlayerGatewayCoach;
import com.mazebert.usecases.player.UpdateProgress.Request;
import com.mazebert.usecases.player.UpdateProgress.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.builders.BuilderFactory.a;

public class UpdateProgressTest extends UsecaseTest<Request, Response> {

    private PlayerGatewayCoach playerGateway;

    @Before
    public void setUp() {
        playerGateway = new PlayerGatewayCoach();
        usecase = new UpdateProgress(playerGateway);
    }

    @Test
    public void nullKey() {
        givenRequest(a(request().withKey(null)));
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.BAD_REQUEST, "Player key must not be null."));
    }

    @Test
    public void playerDoesNotExist() {
        givenRequest(a(request().withKey("unknown")));
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.NOT_FOUND, "A player with this key does not exist."));
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