package com.mazebert.usecases.blackmarket;

import com.mazebert.entities.BlackMarket;
import com.mazebert.entities.BlackMarketOffer;
import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.error.ServiceUnavailable;
import com.mazebert.gateways.*;
import com.mazebert.gateways.transaction.TransactionRunner;
import com.mazebert.plugins.random.RandomNumberGenerator;
import com.mazebert.plugins.time.CurrentDatePlugin;
import com.mazebert.plugins.validation.VersionValidator;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.security.SignResponse;
import com.mazebert.usecases.security.VerifyRequest;
import org.jusecase.Usecase;

import javax.inject.Inject;

public class BuyBlackMarketOffer implements Usecase<BuyBlackMarketOffer.Request, BuyBlackMarketOffer.Response> {
    private final VersionValidator versionValidator = new VersionValidator("1.1.0");
    private final TransactionRunner transactionRunner;
    private final PlayerGateway playerGateway;
    private final BlackMarketOfferGateway blackMarketOfferGateway;
    private final FoilCardGateway foilCardGateway;
    private final BlackMarket blackMarket;

    @Inject
    public BuyBlackMarketOffer(PlayerGateway playerGateway, BlackMarketOfferGateway blackMarketOfferGateway,
                               CurrentDatePlugin currentDatePlugin, BlackMarketSettingsGateway blackMarketSettingsGateway,
                               CardGateway cardGateway, RandomNumberGenerator randomNumberGenerator,
                               TransactionRunner transactionRunner, FoilCardGateway foilCardGateway) {
        this.playerGateway = playerGateway;
        this.blackMarketOfferGateway = blackMarketOfferGateway;
        this.transactionRunner = transactionRunner;
        this.foilCardGateway = foilCardGateway;
        this.blackMarket = new BlackMarket(currentDatePlugin, blackMarketOfferGateway,
                blackMarketSettingsGateway, cardGateway, randomNumberGenerator);
    }

    public Response execute(Request request) {
        validateRequest(request);
        return doTransaction(request);
    }

    private Response doTransaction(Request request) {
        Player player = getPlayer(request);
        BlackMarketOffer offer = getCurrentOffer(player);

        int price = blackMarket.getPrice();
        if (player.getRelics() < price) {
            throw new ServiceUnavailable("Come back when you got my " + price + " relics!");
        }

        FoilCard foilCard = offer.createFoilCard();

        transactionRunner.runAsTransaction(() -> {
            foilCardGateway.addFoilCardToPlayer(player.getId(), foilCard);
            playerGateway.addRelics(player.getId(), -price);
            blackMarketOfferGateway.markOfferAsPurchased(offer, player);
        });

        return createResponse(foilCard, player);
    }

    private Player getPlayer(Request request) {
        Player player = playerGateway.findPlayerByKey(request.key);
        if (player == null) {
            throw new NotFound("Player does not exist.");
        }
        return player;
    }

    private BlackMarketOffer getCurrentOffer(Player player) {
        BlackMarketOffer offer = blackMarket.getOffer();
        if (offer == null) {
            throw new ServiceUnavailable("The black market is not available at the moment.");
        }

        if (blackMarketOfferGateway.isOfferPurchased(offer, player)) {
            throw new ServiceUnavailable("This week's offer is already purchased.");
        }
        return offer;
    }

    private Response createResponse(FoilCard foilCard, Player player) {
        Response response = new Response();
        response.id = foilCard.getCardId();
        response.type = foilCard.getCardType();
        response.relics = playerGateway.getRelics(player.getId());
        response.amount = foilCardGateway.getFoilCardAmount(player.getId(), response.id, response.type);
        return response;
    }

    private void validateRequest(Request request) {
        versionValidator.validate(request.appVersion);
        if (request.key == null) {
            throw new BadRequest("Key must not be null.");
        }
    }

    @VerifyRequest
    @StatusResponse
    @SignResponse
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
