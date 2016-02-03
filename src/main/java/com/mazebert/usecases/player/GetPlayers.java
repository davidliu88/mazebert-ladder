package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.PlayerGateway;
import org.jusecase.Usecase;

import javax.inject.Inject;
import java.util.List;

public class GetPlayers implements Usecase<GetPlayers.Request, GetPlayers.Response> {
    private final PlayerGateway playerGateway;

    public static class Request {
        public int start;
        public int limit;
    }

    public static class Response {
        public List<Player> players;
    }

    @Inject
    public GetPlayers(PlayerGateway playerGateway) {
        this.playerGateway = playerGateway;
    }

    public Response execute(Request request) {
        if (request.start < 0) {
            throw new Error(Type.BAD_REQUEST, "Start parameter must be greater than or equal to 0.");
        }

        if (request.limit <= 0) {
            throw new Error(Type.BAD_REQUEST, "Limit must be greater than 0.");
        }

        Response response = new Response();
        response.players = playerGateway.findPlayers(request.start, request.limit);

        return response;
    }
}
