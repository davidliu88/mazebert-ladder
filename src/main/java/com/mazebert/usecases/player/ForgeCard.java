package com.mazebert.usecases.player;

import com.mazebert.entities.Card;
import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;
import com.mazebert.error.ServiceUnavailable;
import com.mazebert.gateways.CardGateway;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.transaction.TransactionRunner;

import java.util.List;

public class ForgeCard extends AbstractBuyCard<ForgeCard.Request, AbstractBuyCard.Response> {
    private static final int requiredRelics = 20;
    private TransactionRunner transactionRunner;
    private final CardGateway cardGateway;

    public ForgeCard(TransactionRunner transactionRunner, FoilCardGateway foilCardGateway, PlayerGateway playerGateway,
                     CardGateway cardGateway) {
        super(playerGateway, foilCardGateway);
        this.transactionRunner = transactionRunner;
        this.cardGateway = cardGateway;
    }

    @Override
    protected Response doTransaction(Player player, Request request) {
        if (player.getRelics() < requiredRelics) {
            throw new ServiceUnavailable("Come back later when you have enough relics.");
        }

        FoilCard foilCard = rollRandomCard();
        transactionRunner.runAsTransaction(() -> {
            foilCardGateway.addFoilCardToPlayer(player.getId(), foilCard);
            playerGateway.addRelics(player.getId(), -requiredRelics);
        });

        return createResponse(foilCard, player);
    }

    private FoilCard rollRandomCard() {
        List<Card> allCards = cardGateway.findAllCards();
        if (allCards.isEmpty()) {
            throw new ServiceUnavailable("There are no cards available that can be forged.");
        }

        Card card = allCards.get(0);

        return card.createFoilCard();
    }

    @Override
    protected String getRequiredVersion() {
        return "1.0.0";
    }

    public static class Request extends AbstractBuyCard.Request {
    }
}
