package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.PlayerGateway;
import org.jusecase.Usecase;

public class SynchronizePlayer implements Usecase<SynchronizePlayer.Request, SynchronizePlayer.Response> {
    private final PlayerGateway playerGateway;

    public SynchronizePlayer(PlayerGateway playerGateway) {
        this.playerGateway = playerGateway;
    }

    public Response execute(Request request) {
        validateRequest(request);
        return createResponse(request);
    }

    private void validateRequest(Request request) {
        if (request.key == null) {
            throw new Error(Type.BAD_REQUEST, "Player key must not be null");
        }
    }

    private Response createResponse(Request request) {
        Player player = playerGateway.findPlayer(request.key);
        if (player == null) {
            throw new Error(Type.NOT_FOUND, "Player does not exist");
        }

        Response response = new Response();
        addPlayerToResponse(player, response);

        return response;
    }

    private void addPlayerToResponse(Player player, Response response) {
        response.id = player.getId();
        response.name = player.getName();
        response.level = player.getLevel();
        response.experience = player.getExperience();
        response.relics = player.getRelics();
    }


    public static class Request {
        public String key;
    }

    public static class Response {
        public long id;
        public String name;
        public int level;
        public long experience;
        public int relics;
    }
}