package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.plugins.time.CurrentDatePlugin;
import com.mazebert.plugins.time.TimeDeltaFormatter;
import org.jusecase.Usecase;

public class GetPlayerProfile implements Usecase<GetPlayerProfile.Request, GetPlayerProfile.Response> {
    private final PlayerGateway playerGateway;
    private final CurrentDatePlugin currentDatePlugin;
    private final TimeDeltaFormatter timeDeltaFormatter;
    private static final String NOW_PLAYING = "Now playing";
    private static final int NOW_PLAYING_THRESHOLD_IN_MINUTES = 10;

    public GetPlayerProfile(PlayerGateway playerGateway, CurrentDatePlugin currentDatePlugin) {
        this.playerGateway = playerGateway;
        this.currentDatePlugin = currentDatePlugin;
        timeDeltaFormatter = new TimeDeltaFormatter(60 * NOW_PLAYING_THRESHOLD_IN_MINUTES, NOW_PLAYING);
    }

    public Response execute(Request request) {
        Player player = playerGateway.findPlayerById(request.id);
        if (player == null) {
            throw new NotFound("A player with id '" + request.id + "' does not exist.");
        }

        Response response = new Response();
        addPlayerInformationToResponse(player, response);
        return response;
    }

    private void addPlayerInformationToResponse(Player player, Response response) {
        response.name = player.getName();
        response.level = player.getLevel();
        response.experience = player.getExperience();
        response.supporterLevel = player.getSupporterLevel();
        response.relics = player.getRelics();
        response.rank = playerGateway.findPlayerRank(player.getId());
        response.lastPlayed = createLastPlayed(player);
    }

    private String createLastPlayed(Player player) {
        String delta = timeDeltaFormatter.format(player.getLastUpdate(), currentDatePlugin.getCurrentDate());
        if (!NOW_PLAYING.equals(delta)) {
            delta = "Last seen " + delta + " ago";
        }
        return delta;
    }

    public static class Request {
        public long id;
    }

    public static class Response {
        public String name;
        public int level;
        public long experience;
        public int supporterLevel;
        public int relics;
        public int rank;
        public String lastPlayed;
    }
}
