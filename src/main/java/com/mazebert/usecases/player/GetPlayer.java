package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.PlayerGateway;
import org.jusecase.Usecase;

import javax.inject.Inject;

public class GetPlayer implements Usecase<GetPlayer.Request, Player> {
    private final PlayerGateway playerGateway;

    @Inject
    public GetPlayer(PlayerGateway playerGateway) {
        this.playerGateway = playerGateway;
    }

    public Player execute(Request request) {
        if (request.key == null) {
            throw new Error(Type.BAD_REQUEST, "Player key must not be null");
        }

        Player player = playerGateway.findPlayer(request.key);
        if (player == null) {
            throw new Error(Type.NOT_FOUND, "A player with this key could not be found");
        }

        player.setRank(playerGateway.findPlayerRank(player.getId()));
        return player;
    }

    public static class Request {
        public String key;
    }
}
