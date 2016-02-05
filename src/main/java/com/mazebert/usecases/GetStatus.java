package com.mazebert.usecases;

import com.mazebert.entities.Player;
import com.mazebert.entities.PlayerRow;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.PlayerRowGateway;
import org.jusecase.Usecase;

import javax.inject.Inject;
import java.util.List;

public class GetStatus implements Usecase<GetStatus.Request, GetStatus.Response> {
    public static class Request {}

    public static class Response {
        public int totalPlayers;
        public int nowPlaying;
        public List<PlayerRow> nowPlayingPlayers;
    }

    private final PlayerRowGateway playerRowGateway;
    private static final int minutesTolerance = 10;

    @Inject
    public GetStatus(PlayerRowGateway playerRowGateway) {
        this.playerRowGateway = playerRowGateway;
    }

    public Response execute(Request request) {
        Response response = new Response();
        response.totalPlayers = playerRowGateway.getTotalPlayerCount();
        response.nowPlayingPlayers = playerRowGateway.findPlayersNowPlaying(minutesTolerance);
        if (response.nowPlayingPlayers != null) {
            response.nowPlaying = response.nowPlayingPlayers.size();
        }
        return response;
    }
}
