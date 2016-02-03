package com.mazebert.usecases;

import com.mazebert.entities.Player;
import com.mazebert.gateways.PlayerGateway;
import org.jusecase.Usecase;

import javax.inject.Inject;
import java.util.List;

public class GetStatus implements Usecase<GetStatus.Request, GetStatus.Response> {
    public static class Request {}

    public static class Response {
        public int totalPlayers;
        public int nowPlaying;
        public List<Player> nowPlayingPlayers;
    }

    private final PlayerGateway playerGateway;
    private static final int minutesTolerance = 10;

    @Inject
    public GetStatus(PlayerGateway playerGateway) {
        this.playerGateway = playerGateway;
    }

    public Response execute(Request request) {
        Response response = new Response();
        response.totalPlayers = playerGateway.getTotalPlayerCount();
        response.nowPlayingPlayers = playerGateway.findPlayersNowPlaying(minutesTolerance);
        if (response.nowPlayingPlayers != null) {
            response.nowPlaying = response.nowPlayingPlayers.size();
        }
        return response;
    }
}
