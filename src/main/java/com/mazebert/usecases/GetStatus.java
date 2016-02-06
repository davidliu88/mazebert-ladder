package com.mazebert.usecases;

import com.mazebert.entities.PlayerRow;
import com.mazebert.gateways.PlayerRowGateway;
import com.mazebert.plugins.time.CurrentDatePlugin;
import org.jusecase.Usecase;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class GetStatus implements Usecase<GetStatus.Request, GetStatus.Response> {
    public static class Request {}

    public static class Response {
        public int totalPlayers;
        public int nowPlaying;
        public List<PlayerRow> nowPlayingPlayers;
    }

    private final PlayerRowGateway playerRowGateway;
    private final CurrentDatePlugin currentDatePlugin;
    private static final int minutesTolerance = 20;

    @Inject
    public GetStatus(PlayerRowGateway playerRowGateway, CurrentDatePlugin currentDatePlugin) {
        this.playerRowGateway = playerRowGateway;
        this.currentDatePlugin = currentDatePlugin;
    }

    public Response execute(Request request) {
        Response response = new Response();
        response.totalPlayers = playerRowGateway.getTotalPlayerCount();

        Date now = currentDatePlugin.getCurrentDate();
        Date updatedSince = new Date(now.getTime() - minutesTolerance * 60 * 1000);

        response.nowPlayingPlayers = playerRowGateway.findPlayersUpdatedSince(updatedSince);
        if (response.nowPlayingPlayers != null) {
            response.nowPlaying = response.nowPlayingPlayers.size();
        }
        return response;
    }
}