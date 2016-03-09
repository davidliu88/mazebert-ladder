package com.mazebert.usecases.player;

import com.mazebert.entities.*;
import com.mazebert.error.InternalServerError;
import com.mazebert.error.NotFound;
import com.mazebert.gateways.CardGateway;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.plugins.time.CurrentDatePlugin;
import com.mazebert.plugins.time.TimeDeltaFormatter;
import com.mazebert.presenters.jaxrs.response.StatusResponse;
import org.jusecase.Usecase;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class GetPlayerProfile implements Usecase<GetPlayerProfile.Request, GetPlayerProfile.Response> {
    private final PlayerGateway playerGateway;
    private final CardGateway cardGateway;
    private final FoilCardGateway foilCardGateway;
    private final CurrentDatePlugin currentDatePlugin;
    private final TimeDeltaFormatter timeDeltaFormatter;
    private static final String NOW_PLAYING = "Now playing";
    private static final int NOW_PLAYING_THRESHOLD_IN_MINUTES = 10;

    @Inject
    public GetPlayerProfile(PlayerGateway playerGateway, CardGateway cardGateway, FoilCardGateway foilCardGateway, CurrentDatePlugin currentDatePlugin) {
        this.playerGateway = playerGateway;
        this.cardGateway = cardGateway;
        this.foilCardGateway = foilCardGateway;
        this.currentDatePlugin = currentDatePlugin;
        timeDeltaFormatter = new TimeDeltaFormatter(60 * NOW_PLAYING_THRESHOLD_IN_MINUTES, NOW_PLAYING);
    }

    public Response execute(Request request) {
        Player player = playerGateway.findPlayerById(request.id);
        if (player == null) {
            throw new NotFound("A player with id '" + request.id + "' does not exist.");
        }

        Response response = new Response();
        addPlayerInformationToResponse(player, response);
        addFoilCardInformationToResonse(player, response);
        return response;
    }

    private void addPlayerInformationToResponse(Player player, Response response) {
        response.name = player.getName();
        response.level = player.getLevel();
        response.experience = player.getExperience();
        response.supporterLevel = player.getSupporterLevel();
        response.relics = player.getRelics();
        response.rank = playerGateway.findPlayerRank(player.getId());
        response.lastPlayed = createLastPlayed(player);
    }

    private void addFoilCardInformationToResonse(Player player, Response response) {
        List<FoilCard> foilCards = foilCardGateway.getFoilCardsForPlayerId(player.getId());

        List<FoilCard> foilHeroes = new ArrayList<>();
        List<FoilCard> foilItems = new ArrayList<>();
        List<FoilCard> foilPotions = new ArrayList<>();
        List<FoilCard> foilTowers = new ArrayList<>();

        foilCards.stream().forEach(foilCard -> {
            switch (foilCard.getCardType()) {
                case CardType.HERO: foilHeroes.add(foilCard); break;
                case CardType.ITEM: foilItems.add(foilCard); break;
                case CardType.POTION: foilPotions.add(foilCard); break;
                case CardType.TOWER: foilTowers.add(foilCard); break;
            }
        });

        addFoilCards(response, "foilHeroProgress", "foilHeroes", foilHeroes, cardGateway.findAllHeroes());
        addFoilCards(response, "foilItemProgress", "foilItems", foilItems, cardGateway.findAllItems());
        addFoilCards(response, "foilPotionProgress", "foilPotions", foilPotions, cardGateway.findAllPotions());
        addFoilCards(response, "foilTowerProgress", "foilTowers", foilTowers, cardGateway.findAllTowers());
    }

    private void addFoilCards(Response response, String progressField, String cardsField, List<FoilCard> foilCards, List<? extends Card> allCards) {
        List<FoilCardInfo> cards = new ArrayList<>();
        for (FoilCard foilHero : foilCards) {
            Card card = findCardById(foilHero.getCardId(), allCards);
            if (card != null) {
                cards.add(createFoilCardInfo(foilHero, card));
            }
        }

        setResponseField(response, progressField, cards.size() + "/" + allCards.size());
        setResponseField(response, cardsField, cards);
    }

    private void setResponseField(Response response, String fieldName, Object value) {
        try {
            response.getClass().getField(fieldName).set(response, value);
        } catch (Throwable e) {
            throw new InternalServerError("Adjust response field '" + fieldName + "' after refactoring!");
        }
    }

    private FoilCardInfo createFoilCardInfo(FoilCard foilCard, Card card) {
        FoilCardInfo info = new FoilCardInfo();
        info.id = card.getId();
        info.name = card.getName();
        info.rarity = card.getRarity();
        info.amount = foilCard.getAmount();
        return info;
    }

    private Card findCardById(long cardId, List<? extends Card> cards) {
        Optional<? extends Card> result = cards.stream().filter(card -> card.getId() == cardId).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    private String createLastPlayed(Player player) {
        String delta = timeDeltaFormatter.format(player.getLastUpdate(), currentDatePlugin.getCurrentDate());
        if (!NOW_PLAYING.equals(delta)) {
            delta = "Last seen " + delta + " ago";
        }
        return delta;
    }

    @StatusResponse(field = "profile")
    public static class Request {
        public long id;
    }

    public static class Response {
        public String name;
        public int level;
        public long experience;
        public int supporterLevel;
        public int relics;
        public int rank;
        public String lastPlayed;
        public String foilHeroProgress;
        public String foilItemProgress;
        public String foilPotionProgress;
        public String foilTowerProgress;
        public List<FoilCardInfo> foilHeroes;
        public List<FoilCardInfo> foilItems;
        public List<FoilCardInfo> foilPotions;
        public List<FoilCardInfo> foilTowers;
    }

    public static class FoilCardInfo {
        public long id;
        public String name;
        public int rarity;
        public int amount;
    }
}
