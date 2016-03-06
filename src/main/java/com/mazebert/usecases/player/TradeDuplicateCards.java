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
import com.mazebert.plugins.validation.VersionValidator;
import org.jusecase.Usecase;

import java.util.List;

public class TradeDuplicateCards implements Usecase<TradeDuplicateCards.Request, TradeDuplicateCards.Response> {
    private final PlayerGateway playerGateway;
    private final CardGateway cardGateway;
    private final FoilCardGateway foilCardGateway;
    private final VersionValidator versionValidator = new VersionValidator("1.0.0");


    public TradeDuplicateCards(PlayerGateway playerGateway, CardGateway cardGateway, FoilCardGateway foilCardGateway) {
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

        if (request.isOffer) {
            return createOfferResponse(player);
        } else {
            return null;
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
        for (FoilCard foilCard : foilCards) {
            if (foilCard.getAmount() > 1) {
                addFoilCardToOffer(offer, foilCard);
            }
        }

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

    private void validateRequest(Request request) {
        versionValidator.validate(request.appVersion);
        if (request.key == null) {
            throw new BadRequest("Key must not be null.");
        }
    }

    public static class Request {
        public String appVersion;
        public String key;
        public boolean isOffer;
    }

    public static class Response {
        public Offer offer;
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
