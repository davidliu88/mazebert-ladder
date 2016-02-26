package com.mazebert.usecases;

import com.mazebert.entities.PlayerRow;
import com.mazebert.gateways.mocks.PlayerRowGatewayMock;
import com.mazebert.plugins.time.mocks.CurrentDatePluginMock;
import com.mazebert.usecases.GetStatus.Request;
import com.mazebert.usecases.GetStatus.Response;
import org.junit.Before;
import org.junit.Test;
import org.jusecase.UsecaseTest;
import org.jusecase.builders.Builder;

import java.util.List;

import static com.mazebert.builders.BuilderFactory.playerRow;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.*;

public class GetStatusTest extends UsecaseTest<Request, Response> {
    private PlayerRowGatewayMock playerRowGateway = new PlayerRowGatewayMock();
    private CurrentDatePluginMock currentDatePlugin = new CurrentDatePluginMock();

    @Before
    public void setUp() {
        usecase = new GetStatus(playerRowGateway, currentDatePlugin);

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
        currentDatePlugin.givenCurrentDate(a(date().with("2015-10-10 08:00:00")));
        List<PlayerRow> players = a(list(
                a(playerRow()),
                a(playerRow()),
                a(playerRow())
        ));
        playerRowGateway.givenPlayerUpdatedSince(a(date().with("2015-10-10 07:40:00")), players);

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