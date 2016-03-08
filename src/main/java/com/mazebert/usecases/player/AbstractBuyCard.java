package com.mazebert.usecases.player;

import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.plugins.validation.VersionValidator;
import org.jusecase.Usecase;

public abstract class AbstractBuyCard<RequestType extends AbstractBuyCard.Request, ResponseType extends AbstractBuyCard.Response> implements Usecase<RequestType, ResponseType> {
    private final VersionValidator versionValidator = new VersionValidator(getRequiredVersion());
    protected final PlayerGateway playerGateway;
    protected final FoilCardGateway foilCardGateway;

    protected AbstractBuyCard(PlayerGateway playerGateway, FoilCardGateway foilCardGateway) {
        this.playerGateway = playerGateway;
        this.foilCardGateway = foilCardGateway;
    }

    @Override
    public ResponseType execute(RequestType request) {
        validateRequest(request);
        return doTransaction(getPlayer(request), request);
    }

    protected abstract ResponseType doTransaction(Player player, RequestType request);

    protected abstract String getRequiredVersion();

    protected void validateRequest(RequestType request) {
        versionValidator.validate(request.appVersion);
        if (request.key == null) {
            throw new BadRequest("Key must not be null.");
        }
    }

    private Player getPlayer(Request request) {
        Player player = playerGateway.findPlayerByKey(request.key);
        if (player == null) {
            throw new NotFound("Player does not exist.");
        }
        return player;
    }

    protected Response createResponse(FoilCard foilCard, Player player) {
        Response response = new Response();
        response.id = foilCard.getCardId();
        response.type = foilCard.getCardType();
        response.relics = playerGateway.getRelics(player.getId());
        response.amount = foilCardGateway.getFoilCardAmount(player.getId(), response.id, response.type);
        return response;
    }

    public static class Request {
        public String appVersion;
        public String key;
    }

    public static class Response {
        public long id;
        public int type;
        public int relics;
        public int amount;
    }
}
