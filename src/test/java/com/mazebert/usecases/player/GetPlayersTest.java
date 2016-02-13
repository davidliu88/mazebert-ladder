package com.mazebert.usecases.player;

import com.mazebert.entities.PlayerRow;
import com.mazebert.error.BadRequest;
import com.mazebert.gateways.PlayerRowGatewayCoach;
import com.mazebert.usecases.player.GetPlayers.Request;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.playerRow;
import static org.junit.Assert.assertEquals;
import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.listWith;

public class GetPlayersTest extends UsecaseTest<Request, List<PlayerRow>> {
    private PlayerRowGatewayCoach playerGateway = new PlayerRowGatewayCoach();

    @Before
    public void setUp() {
        usecase = new GetPlayers(playerGateway);
    }

    @Test
    public void negativeStart() {
        givenRequest(a(request().withStart(-1)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Start parameter must be greater than or equal to 0."));
    }

    @Test
    public void zeroLimit() {
        givenRequest(a(request().withLimit(0)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Limit must be greater than 0."));
    }

    @Test
    public void negativeLimit() {
        givenRequest(a(request().withLimit(-1)));
        whenRequestIsExecuted();
        thenErrorIs(new BadRequest("Limit must be greater than 0."));
    }

    @Test
    public void parametersArePassedToGateway() {
        givenRequest(a(request()
                .withStart(1000)
                .withLimit(500)));
        List<PlayerRow> expectedPlayers = a(listWith(
                a(playerRow()),
                a(playerRow()),
                a(playerRow())
        ));
        playerGateway.givenPlayers(1000, 500, expectedPlayers);

        whenRequestIsExecuted();

        assertEquals(expectedPlayers, response);
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