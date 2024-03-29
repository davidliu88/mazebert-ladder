package com.mazebert.usecases.player;

import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.error.KeyAlreadyExists;
import com.mazebert.plugins.random.PlayerKeyGenerator;
import com.mazebert.plugins.validation.VersionValidator;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.security.VerifyRequest;
import org.jusecase.Usecase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CreateAccount implements Usecase<CreateAccount.Request, CreateAccount.Response> {
    private final VersionValidator versionValidator = new VersionValidator("0.4.0");
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
        response.id = player.getId();
        response.key = player.getKey();
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
        player.setName(request.name);
        player.setLevel(request.level);
        player.setExperience(request.experience);
        player.setKey(keyGenerator.createPlayerKey());
        return player;
    }

    private void validateRequest(Request request) {
        if (request.name == null) {
            throw new BadRequest("Player name must not be null");
        } else if (request.name.isEmpty()) {
            throw new BadRequest("Player name must not be empty");
        }

        if (request.level <= 0) {
            throw new BadRequest("Player level must be greater than 0");
        }

        if (request.experience <= 0) {
            throw new BadRequest("Player experience must be greater than 0");
        }

        versionValidator.validate(request.appVersion);
    }

    @VerifyRequest
    @StatusResponse
    public static class Request {
        public String appVersion;
        public String name;
        public int level;
        public long experience;
    }

    public static class Response {
        public long id;
        public String key;
    }
}
