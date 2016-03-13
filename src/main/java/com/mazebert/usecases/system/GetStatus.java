package com.mazebert.usecases.system;

import com.mazebert.entities.PlayerRowSimple;
import com.mazebert.gateways.PlayerRowGateway;
import com.mazebert.plugins.time.CurrentDatePlugin;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import org.jusecase.Usecase;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.List;

@Singleton
public class GetStatus implements Usecase<GetStatus.Request, GetStatus.Response> {
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

    @StatusResponse
    public static class Request {}

    public static class Response {
        public int totalPlayers;
        public int nowPlaying;
        public List<PlayerRowSimple> nowPlayingPlayers;
    }
}
