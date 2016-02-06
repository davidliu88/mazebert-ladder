package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.PlayerGatewayCoach;
import com.mazebert.usecases.player.GetPlayer.Request;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.builders.BuilderFactory.a;

public class GetPlayerTest extends UsecaseTest<Request, Player> {
    private PlayerGatewayCoach playerGateway;

    @Before
    public void setUp() {
        playerGateway = new PlayerGatewayCoach();
        usecase = new GetPlayer(playerGateway);
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
        thenErrorIs(new Error(Type.NOT_FOUND, "A player with this key could not be found"));
    }

    @Test
    public void playerIsReturnedFromGateway() {
        Player expected = a(player().casid());
        givenRequest(a(request()));
        playerGateway.givenPlayer(expected);

        whenRequestIsExecuted();

        thenResponseIs(expected);
    }

    @Test
    public void playerRankIsReturnedFromGateway() {
        givenRequest(a(request()));
        playerGateway.givenPlayer(a(player().casid()));
        playerGateway.givenPlayerRank(133, a(player().casid()));

        whenRequestIsExecuted();
        assertEquals(133, response.getRank());
    }

    private RequestBuilder request() {
        return new RequestBuilder().golden();
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        public RequestBuilder golden() {
            return this.withKey("abcdef");
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