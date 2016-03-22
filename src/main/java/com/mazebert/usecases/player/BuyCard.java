package com.mazebert.usecases.player;

import com.mazebert.entities.Card;
import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.CardGateway;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.transaction.TransactionRunner;
import com.mazebert.plugins.validation.CardTypeValidator;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.security.SignResponse;
import com.mazebert.usecases.security.VerifyRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BuyCard extends AbstractBuyCard<BuyCard.Request, AbstractBuyCard.Response> {
    private final CardTypeValidator cardTypeValidator = new CardTypeValidator();
    private final CardGateway cardGateway;
    private final TransactionRunner transactionRunner;

    @Inject
    protected BuyCard(PlayerGateway playerGateway, FoilCardGateway foilCardGateway, CardGateway cardGateway, TransactionRunner transactionRunner) {
        super(playerGateway, foilCardGateway);
        this.cardGateway = cardGateway;
        this.transactionRunner = transactionRunner;
    }

    @Override
    protected void validateRequest(Request request) {
        super.validateRequest(request);
        cardTypeValidator.validate(request.cardType);
    }

    @Override
    protected Response doTransaction(Player player, Request request) {
        Card card = cardGateway.findCard(request.cardId, request.cardType);
        if (card == null) {
            throw new NotFound("The requested card does not exist.");
        }

        FoilCard foilCard = card.createFoilCard();
        transactionRunner.runAsTransaction(() -> buyCard(player, foilCard, card.getPrice()));
        return createResponse(foilCard, player);
    }

    @Override
    protected String getRequiredVersion() {
        return "1.4.0";
    }

    @VerifyRequest
    @StatusResponse
    @SignResponse
    public static class Request extends AbstractBuyCard.Request {
        public long cardId;
        public int cardType;
    }
}
