package com.mazebert.usecases.shop;

import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.plugins.validation.VersionValidator;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.security.SignResponse;
import com.mazebert.usecases.security.VerifyRequest;
import org.jusecase.Usecase;

import javax.inject.Inject;

public class PrepareShopTransaction implements Usecase<PrepareShopTransaction.Request, PrepareShopTransaction.Response> {
    private final VersionValidator versionValidator = new VersionValidator("1.0.0");
    private final PlayerGateway playerGateway;

    @Inject
    public PrepareShopTransaction(PlayerGateway playerGateway) {
        this.playerGateway = playerGateway;
    }

    @Override
    public Response execute(Request request) {
        validateRequest(request);

        if ("GooglePlay".equals(request.store)) {
            return createGooglePlayResponse(request);
        }

        throw new NotFound("Store does not exist.");
    }

    private Response createGooglePlayResponse(Request request) {
        Player player = getPlayer(request);

        Response response = new Response();
        response.developerPayload = player.getId() + "-" + request.productId;
        return response;
    }

    private Player getPlayer(Request request) {
        Player player = playerGateway.findPlayerByKey(request.key);
        if (player == null) {
            throw new NotFound("Player does not exist.");
        }
        return player;
    }

    private void validateRequest(Request request) {
        versionValidator.validate(request.appVersion);
        if (request.key == null) {
            throw new BadRequest("Key must not be null.");
        }
        if (request.store == null) {
            throw new BadRequest("Store must not be null.");
        }
        if (request.productId == null) {
            throw new BadRequest("Product ID must not be null.");
        }
    }

    @VerifyRequest
    @StatusResponse
    @SignResponse
    public static class Request {
        public String appVersion;
        public String key;
        public String store;
        public String productId;
    }

    public static class Response {
        public String developerPayload;
    }
}
