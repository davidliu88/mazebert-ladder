package com.mazebert.usecases.blackmarket;

import com.mazebert.entities.BlackMarket;
import com.mazebert.entities.BlackMarketOffer;
import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;
import com.mazebert.error.ServiceUnavailable;
import com.mazebert.gateways.*;
import com.mazebert.plugins.random.RandomNumberGenerator;
import com.mazebert.plugins.time.CurrentDatePlugin;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.player.AbstractBuyCard;
import com.mazebert.usecases.security.SignResponse;
import com.mazebert.usecases.security.VerifyRequest;
import org.jusecase.transaction.TransactionRunner;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BuyBlackMarketOffer extends AbstractBuyCard<BuyBlackMarketOffer.Request, AbstractBuyCard.Response> {
    private final TransactionRunner transactionRunner;
    private final BlackMarketOfferGateway blackMarketOfferGateway;
    private final BlackMarket blackMarket;

    @Inject
    public BuyBlackMarketOffer(PlayerGateway playerGateway, BlackMarketOfferGateway blackMarketOfferGateway,
                               CurrentDatePlugin currentDatePlugin, BlackMarketSettingsGateway blackMarketSettingsGateway,
                               CardGateway cardGateway, RandomNumberGenerator randomNumberGenerator,
                               TransactionRunner transactionRunner, FoilCardGateway foilCardGateway) {
        super(playerGateway, foilCardGateway);
        this.blackMarketOfferGateway = blackMarketOfferGateway;
        this.transactionRunner = transactionRunner;
        this.blackMarket = new BlackMarket(currentDatePlugin, blackMarketOfferGateway,
                blackMarketSettingsGateway, cardGateway, randomNumberGenerator);
    }

    @Override
    protected String getRequiredVersion() {
        return "1.1.0";
    }

    @Override
    protected Response doTransaction(Player player, Request request) {
        BlackMarketOffer offer = getCurrentOffer(player);

        int price = blackMarket.getPrice();
        FoilCard foilCard = offer.createFoilCard();
        transactionRunner.runAsTransaction(() -> {
            buyCard(player, foilCard, price);
            blackMarketOfferGateway.markOfferAsPurchased(offer, player);
        });

        return createResponse(foilCard, player);
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

    @VerifyRequest
    @StatusResponse
    @SignResponse
    public static class Request extends AbstractBuyCard.Request {
    }
}
