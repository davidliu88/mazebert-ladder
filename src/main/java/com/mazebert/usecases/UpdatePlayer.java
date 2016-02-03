package com.mazebert.usecases;

import com.mazebert.entities.Player;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.PlayerGateway;
import org.jusecase.Usecase;

public class UpdatePlayer implements Usecase<UpdatePlayer.Request, UpdatePlayer.Response> {
    private final PlayerGateway playerGateway;

    public static class Request {
        public String key;
        public int level;
        public long experience;
    }

    public static class Response {
    }

    public UpdatePlayer(PlayerGateway playerGateway) {
        this.playerGateway = playerGateway;
    }

    public Response execute(Request request) {
        if (request.key == null) {
            throw new Error(Type.BAD_REQUEST, "Player key must not be null.");
        }

        Player player = playerGateway.findPlayer(request.key);
        if (player == null) {
            throw new Error(Type.NOT_FOUND, "A player with this key does not exist.");
        }

        player.setLevel(Math.max(player.getLevel(), request.level));
        player.setExperience(Math.max(player.getExperience(), request.experience));
        playerGateway.updatePlayer(player);

        return new Response();
    }
}
