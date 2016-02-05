package com.mazebert.usecases;

import com.mazebert.entities.PlayerRow;
import com.mazebert.gateways.PlayerRowGatewayCoach;
import com.mazebert.usecases.GetStatus.Request;
import com.mazebert.usecases.GetStatus.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.playerRow;
import static org.junit.Assert.assertEquals;
import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.listWith;

public class GetStatusTest extends UsecaseTest<Request, Response> {
    private PlayerRowGatewayCoach playerRowGateway;

    @Before
    public void setUp() {
        playerRowGateway = new PlayerRowGatewayCoach();
        usecase = new GetStatus(playerRowGateway);

        givenRequest(a(request()));
    }

    @Test
    public void totalPlayerCountIsReturned() {
        playerRowGateway.givenTotalPlayerCount(100);
        whenRequestIsExecuted();
        assertEquals(100, response.totalPlayers);
    }

    @Test
    public void nowPlayingPlayersAreReturned() {
        List<PlayerRow> players = a(listWith(
                a(playerRow()),
                a(playerRow()),
                a(playerRow())
        ));
        playerRowGateway.givenPlayersNowPlaying(10, players);

        whenRequestIsExecuted();

        assertEquals(3, response.nowPlaying);
        assertEquals(players, response.nowPlayingPlayers);
    }

    private RequestBuilder request() {
        return new RequestBuilder().golden();
    }

    private static class RequestBuilder implements Builder<Request> {
        private Request request = new Request();

        public RequestBuilder golden() {
            return this;
        }

        public Request build() {
            return request;
        }
    }
}