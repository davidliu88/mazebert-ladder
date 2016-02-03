package com.mazebert.usecases;

import com.mazebert.entities.Player;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.PlayerGatewayCoach;
import com.mazebert.usecases.GetPlayers.Request;
import com.mazebert.usecases.GetPlayers.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.listWith;

public class GetPlayersTest extends UsecaseTest<Request, Response> {

    private PlayerGatewayCoach playerGateway;

    @Before
    public void setUp() {
        playerGateway = new PlayerGatewayCoach();

        usecase = new GetPlayers(playerGateway);
    }

    @Test
    public void negativeStart() {
        givenRequest(a(request().withStart(-1)));
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.BAD_REQUEST, "Start parameter must be greater than or equal to 0."));
    }

    @Test
    public void zeroLimit() {
        givenRequest(a(request().withLimit(0)));
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.BAD_REQUEST, "Limit must be greater than 0."));
    }

    @Test
    public void negativeLimit() {
        givenRequest(a(request().withLimit(-1)));
        whenRequestIsExecuted();
        thenErrorIs(new Error(Type.BAD_REQUEST, "Limit must be greater than 0."));
    }

    @Test
    public void parametersArePassedToGateway() {
        givenRequest(a(request()
                .withStart(1000)
                .withLimit(500)));
        List<Player> expectedPlayers = a(listWith(
                a(player()),
                a(player()),
                a(player())
        ));
        playerGateway.givenPlayers(1000, 500, expectedPlayers);

        whenRequestIsExecuted();

        assertEquals(expectedPlayers, response.players);
    }

    private RequestBuilder request() {
        return new RequestBuilder().golden();
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        public RequestBuilder golden() {
            return this
                    .withStart(0)
                    .withLimit(100);
        }

        public RequestBuilder withStart(int start) {
            request.start = start;
            return this;
        }

        public RequestBuilder withLimit(int limit) {
            request.limit = limit;
            return this;
        }

        public Request build() {
            return request;
        }
    }
}