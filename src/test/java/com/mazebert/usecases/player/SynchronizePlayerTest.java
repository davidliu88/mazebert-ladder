package com.mazebert.usecases.player;

import com.mazebert.builders.BuilderFactory;
import com.mazebert.entities.Player;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.PlayerGatewayCoach;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;
import com.mazebert.usecases.player.SynchronizePlayer.Request;
import com.mazebert.usecases.player.SynchronizePlayer.Response;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.builders.BuilderFactory.a;

public class SynchronizePlayerTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayCoach playerGateway;

    @Before
    public void setUp() {
        playerGateway = new PlayerGatewayCoach();
        usecase = new SynchronizePlayer(playerGateway);
    }

    @Test
    public void nullKey() {
        givenRequest(a(request().withKey(null)));
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.BAD_REQUEST, "Player key must not be null"));
    }

    @Test
    public void playerDoesNotExist() {
        givenRequest(a(request()));
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

    private RequestBuilder request() {
        return new RequestBuilder().golden();
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
}