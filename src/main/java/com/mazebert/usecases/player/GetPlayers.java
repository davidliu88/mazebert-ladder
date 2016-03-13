package com.mazebert.usecases.player;

import com.mazebert.entities.PlayerRow;
import com.mazebert.error.BadRequest;
import com.mazebert.gateways.PlayerRowGateway;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import org.jusecase.Usecase;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class GetPlayers implements Usecase<GetPlayers.Request, List<PlayerRow>> {
    private final PlayerRowGateway playerRowGateway;

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

    @StatusResponse(field = "players")
    public static class Request {
        public int start;
        public int limit;
    }
}
