package com.mazebert.usecases;

import com.mazebert.entities.Player;
import com.mazebert.gateways.PlayerGatewayCoach;
import com.mazebert.usecases.GetStatus.Request;
import com.mazebert.usecases.GetStatus.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.player;
import static org.junit.Assert.assertEquals;
import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.listWith;

public class GetStatusTest extends UsecaseTest<Request, Response> {
    private PlayerGatewayCoach playerGateway;

    @Before
    public void setUp() {
        playerGateway = new PlayerGatewayCoach();
        usecase = new GetStatus(playerGateway);

        givenRequest(a(request()));
    }

    @Test
    public void totalPlayerCountIsReturned() {
        playerGateway.givenTotalPlayerCount(100);
        whenRequestIsExecuted();
        assertEquals(100, response.totalPlayers);
    }

    @Test
    public void nowPlayingPlayersAreReturned() {
        List<Player> players = a(listWith(
                a(player().withName("Player 1")),
                a(player().withName("Player 2")),
                a(player().withName("Player 3"))
        ));
        playerGateway.givenPlayersNowPlaying(10, players);

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