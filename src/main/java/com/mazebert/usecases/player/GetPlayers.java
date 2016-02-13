package com.mazebert.usecases.player;

import com.mazebert.entities.PlayerRow;
import com.mazebert.error.BadRequest;
import com.mazebert.gateways.PlayerRowGateway;
import org.jusecase.Usecase;

import javax.inject.Inject;
import java.util.List;

public class GetPlayers implements Usecase<GetPlayers.Request, List<PlayerRow>> {
    private final PlayerRowGateway playerRowGateway;

    public static class Request {
        public int start;
        public int limit;
    }

    @Inject
    public GetPlayers(PlayerRowGateway playerRowGateway) {
        this.playerRowGateway = playerRowGateway;
    }

    public List<PlayerRow> execute(Request request) {
        if (request.start < 0) {
            throw new BadRequest("Start parameter must be greater than or equal to 0.");
        }

        if (request.limit <= 0) {
            throw new BadRequest("Limit must be greater than 0.");
        }

        return playerRowGateway.findPlayers(request.start, request.limit);
    }
}
