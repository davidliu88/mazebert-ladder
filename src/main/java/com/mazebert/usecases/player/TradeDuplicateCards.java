package com.mazebert.usecases.player;

import com.mazebert.entities.Card;
import com.mazebert.entities.CardRarity;
import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;
import com.mazebert.error.BadRequest;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.CardGateway;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.transaction.TransactionRunner;
import com.mazebert.plugins.validation.VersionValidator;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.player.response.FoilCardsResponse;
import com.mazebert.usecases.security.SignResponse;
import com.mazebert.usecases.security.VerifyRequest;
import org.jusecase.Usecase;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TradeDuplicateCards implements Usecase<TradeDuplicateCards.Request, TradeDuplicateCards.Response> {
    private final TransactionRunner transactionRunner;
    private final PlayerGateway playerGateway;
    private final CardGateway cardGateway;
    private final FoilCardGateway foilCardGateway;
    private final VersionValidator versionValidator = new VersionValidator("1.0.0");

    @Inject
    public TradeDuplicateCards(TransactionRunner transactionRunner, PlayerGateway playerGateway,
                               CardGateway cardGateway, FoilCardGateway foilCardGateway) {
        this.transactionRunner = transactionRunner;
        this.playerGateway = playerGateway;
        this.cardGateway = cardGateway;
        this.foilCardGateway = foilCardGateway;
    }

    public Response execute(Request request) {
        validateRequest(request);

        Player player = playerGateway.findPlayerByKey(request.key);
        if (player == null) {
            throw new NotFound("Player does not exist.");
        }

        if (request.offer) {
            return createOfferResponse(player);
        } else {
            return createTradeResponse(player);
        }
    }

    private Response createOfferResponse(Player player) {
        Response response = new Response();
        response.offer = createOffer(player);
        return response;
    }

    private Offer createOffer(Player player) {
        Offer offer = new Offer();

        List<FoilCard> foilCards = foilCardGateway.getFoilCardsForPlayerId(player.getId());
        foilCards.stream()
                .filter(foilCard -> foilCard.getAmount() > 1)
                .forEach(foilCard -> addFoilCardToOffer(offer, foilCard));

        return offer;
    }

    private void addFoilCardToOffer(Offer offer, FoilCard foilCard) {
        Card card = cardGateway.findCard(foilCard.getCardId(), foilCard.getCardType());
        if (card != null) {
            int amount = foilCard.getAmount() - 1;
            int reward = card.getTradeValue() * amount;

            OfferPart offerPart = offer.rarities[card.getRarity() - 1];
            offerPart.amount += amount;
            offerPart.reward += reward;

            offer.total += reward;
        }
    }

    private Response createTradeResponse(Player player) {
        Response response = new Response();
        doTradeCardsTransaction(player, response);
        return response;
    }

    private void doTradeCardsTransaction(Player player, Response response) {
        Offer offer = createOffer(player);
        transactionRunner.runAsTransaction(() -> {
            foilCardGateway.setAmountOfAllPlayerFoilCards(player.getId(), 1);
            playerGateway.addRelics(player.getId(), offer.total);
        });
        response.addFoilCards(player, foilCardGateway);
        response.relics = playerGateway.getRelics(player.getId());
    }


    private void validateRequest(Request request) {
        versionValidator.validate(request.appVersion);
        if (request.key == null) {
            throw new BadRequest("Key must not be null.");
        }
    }

    @VerifyRequest
    @SignResponse
    @StatusResponse
    public static class Request {
        public String appVersion;
        public String key;
        public boolean offer;
    }

    public static class Response extends FoilCardsResponse {
        public Offer offer;
        public int relics;
    }

    public static class Offer {
        public int total;
        public OfferPart[] rarities;

        public Offer() {
            rarities = new OfferPart[CardRarity.COUNT];
            for (int i = 0; i < rarities.length; ++i) {
                rarities[i] = new OfferPart();
            }
        }
    }

    public static class OfferPart {
        public int amount;
        public int reward;
    }
}
