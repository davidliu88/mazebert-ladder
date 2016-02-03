package com.mazebert.usecases;

import com.mazebert.entities.Player;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.error.KeyAlreadyExists;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.plugins.random.PlayerKeyGenerator;
import org.jusecase.Usecase;

import javax.inject.Inject;

public class CreateAccount implements Usecase<CreateAccount.Request, CreateAccount.Response> {
    public static class Request {
        public String name;
        public int level;
        public long experience;
    }

    public static class Response {
        public int id;
        public String key;
    }

    private final PlayerGateway playerGateway;
    private final PlayerKeyGenerator keyGenerator;

    @Inject
    public CreateAccount(PlayerGateway playerGateway, PlayerKeyGenerator keyGenerator) {
        this.playerGateway = playerGateway;
        this.keyGenerator = keyGenerator;
    }

    public Response execute(Request request) {
        validateRequest(request);

        Player player = createNewUniquePlayer(request);
        return success(player);
    }

    private Response success(Player player) {
        Response response = new Response();
        response.id = player.id;
        response.key = player.key;
        return response;
    }

    private Player createNewUniquePlayer(Request request) {
        Player player = null;
        while (player == null) {
            try {
                player = playerGateway.addPlayer(createNewPlayer(request));
            } catch (KeyAlreadyExists e) {
                player = null;
            }
        }
        return player;
    }

    private Player createNewPlayer(Request request) {
        Player player = new Player();
        player.name = request.name;
        player.level = request.level;
        player.experience = request.experience;
        player.key = keyGenerator.createPlayerKey();
        return player;
    }

    private void validateRequest(Request request) {
        if (request.name == null) {
            throw new Error(Type.BAD_REQUEST, "Player name must not be null");
        } else if (request.name.isEmpty()) {
            throw new Error(Type.BAD_REQUEST, "Player name must not be empty");
        }

        if (request.level <= 0) {
            throw new Error(Type.BAD_REQUEST, "Player level must be greater than 0");
        }

        if (request.experience <= 0) {
            throw new Error(Type.BAD_REQUEST, "Player experience must be greater than 0");
        }
    }
}
