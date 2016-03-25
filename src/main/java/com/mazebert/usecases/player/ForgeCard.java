package com.mazebert.usecases.player;

import com.mazebert.entities.*;
import com.mazebert.error.ServiceUnavailable;
import com.mazebert.gateways.CardGateway;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import org.jusecase.transaction.TransactionRunner;
import com.mazebert.plugins.random.RandomNumberGenerator;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import com.mazebert.usecases.security.SignResponse;
import com.mazebert.usecases.security.VerifyRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ForgeCard extends AbstractBuyCard<ForgeCard.Request, AbstractBuyCard.Response> {
    private static final int requiredRelics = 20;
    private final TransactionRunner transactionRunner;
    private final CardGateway cardGateway;
    private final RandomNumberGenerator randomNumberGenerator;

    private static final double LAST_COMMON_ROLL = 0.4;
    private static final double LAST_UNCOMMON_ROLL = 0.7;
    private static final double LAST_RARE_ROLL = 0.9;
    private static final double LAST_UNIQUE_ROLL = 0.98;

    @Inject
    public ForgeCard(TransactionRunner transactionRunner, FoilCardGateway foilCardGateway, PlayerGateway playerGateway,
                     CardGateway cardGateway, RandomNumberGenerator randomNumberGenerator) {
        super(playerGateway, foilCardGateway);
        this.transactionRunner = transactionRunner;
        this.cardGateway = cardGateway;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @Override
    protected Response doTransaction(Player player, Request request) {
        FoilCard foilCard = rollRandomCard(request);
        transactionRunner.runAsTransaction(() -> buyCard(player, foilCard, requiredRelics));

        return createResponse(foilCard, player);
    }

    private FoilCard rollRandomCard(Request request) {
        List<Card> allCards = cardGateway.findAllCards();
        Card card = rollRandomCard(allCards, new Version(request.appVersion));

        return card.createFoilCard();
    }

    private Card rollRandomCard(List<Card> allCards, Version minimumVersion) {
        removeIncompatibleCards(allCards, minimumVersion);

        if (allCards.isEmpty()) {
            throw new ServiceUnavailable("There are no cards available that can be forged.");
        }

        return pickRandomCardWithRarity(allCards, rollCardRarity());
    }

    private int rollCardRarity() {
        double diceRoll = randomNumberGenerator.randomDouble();
        if (diceRoll < LAST_COMMON_ROLL) return CardRarity.COMMON;
        if (diceRoll < LAST_UNCOMMON_ROLL) return CardRarity.UNCOMMON;
        if (diceRoll < LAST_RARE_ROLL) return CardRarity.RARE;
        if (diceRoll < LAST_UNIQUE_ROLL) return CardRarity.UNIQUE;
        return CardRarity.LEGENDARY;
    }

    private Card pickRandomCardWithRarity(List<Card> allCards, int rarity) {
        if (rarity < 0) {
            return pickRandomCard(allCards);
        }

        List<Card> possibleCards = allCards.stream().filter(card -> card.getRarity() == rarity).collect(Collectors.toList());

        if (possibleCards.isEmpty()) {
            return pickRandomCardWithRarity(allCards, rarity - 1);
        } else {
            return pickRandomCard(possibleCards);
        }
    }

    private Card pickRandomCard(List<Card> cards) {
        return cards.get(randomNumberGenerator.randomInteger(0, cards.size() - 1));
    }

    private void removeIncompatibleCards(List<Card> allCards, Version minimumVersion) {
        for (Iterator<Card> it = allCards.iterator(); it.hasNext(); ) {
            Card card = it.next();
            if (!card.isForgeable() || minimumVersion.compareTo(new Version(card.getSinceVersion() + ".0")) < 0) {
                it.remove();
            }
        }
    }

    @Override
    protected String getRequiredVersion() {
        return "1.0.0";
    }

    @VerifyRequest
    @StatusResponse
    @SignResponse
    public static class Request extends AbstractBuyCard.Request {
    }
}
