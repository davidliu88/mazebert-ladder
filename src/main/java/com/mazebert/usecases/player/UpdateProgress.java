package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.plugins.time.CurrentDatePlugin;
import com.mazebert.usecases.security.SecureRequest;
import org.jusecase.Usecase;

import javax.inject.Inject;

public class UpdateProgress implements Usecase<UpdateProgress.Request, UpdateProgress.Response> {
    private final PlayerGateway playerGateway;
    private final CurrentDatePlugin currentDatePlugin;

    @SecureRequest
    public static class Request {
        public String key;
        public int level;
        public long experience;
    }

    public static class Response {
    }

    @Inject
    public UpdateProgress(PlayerGateway playerGateway, CurrentDatePlugin currentDatePlugin) {
        this.playerGateway = playerGateway;
        this.currentDatePlugin = currentDatePlugin;
    }

    public Response execute(Request request) {
        if (request.key == null) {
            throw new BadRequest("Player key must not be null.");
        }

        Player player = playerGateway.findPlayer(request.key);
        if (player == null) {
            throw new NotFound("A player with this key does not exist.");
        }

        player.setLevel(Math.max(player.getLevel(), request.level));
        player.setExperience(Math.max(player.getExperience(), request.experience));
        player.setLastUpdate(currentDatePlugin.getCurrentDate());
        playerGateway.updatePlayer(player);

        return new Response();
    }
}
