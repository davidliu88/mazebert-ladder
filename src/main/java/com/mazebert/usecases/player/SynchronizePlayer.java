package com.mazebert.usecases.player;

import com.mazebert.entities.*;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.QuestGateway;
import com.mazebert.plugins.random.DailyQuestGenerator;
import com.mazebert.plugins.time.CurrentDatePlugin;
import com.mazebert.plugins.time.TimeZoneParser;
import org.jusecase.Usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class SynchronizePlayer implements Usecase<SynchronizePlayer.Request, SynchronizePlayer.Response> {
    private final PlayerGateway playerGateway;
    private final FoilCardGateway foilCardGateway;
    private final QuestGateway questGateway;
    private final DailyQuestGenerator dailyQuestGenerator;
    private final TimeZoneParser timeZoneParser;

    public SynchronizePlayer(PlayerGateway playerGateway,
                             FoilCardGateway foilCardGateway,
                             QuestGateway questGateway,
                             CurrentDatePlugin currentDatePlugin) {
        this.playerGateway = playerGateway;
        this.foilCardGateway = foilCardGateway;
        this.questGateway = questGateway;
        this.dailyQuestGenerator = new DailyQuestGenerator(questGateway, foilCardGateway, currentDatePlugin);
        timeZoneParser = new TimeZoneParser();
    }

    public Response execute(Request request) {
        validateRequest(request);
        return createResponse(request);
    }

    private void validateRequest(Request request) {
        if (request.key == null) {
            throw new Error(Type.BAD_REQUEST, "Player key must not be null");
        }
    }

    private Response createResponse(Request request) {
        Player player = playerGateway.findPlayer(request.key);
        if (player == null) {
            throw new Error(Type.NOT_FOUND, "Player does not exist");
        }

        Version appVersion = new Version(request.appVersion);
        TimeZone timeZone = timeZoneParser.parseAppOffset(request.timeZoneOffset);

        Response response = new Response();
        addPlayerToResponse(player, response);
        addFoilCardsToResponse(player, response);
        addQuestsToResponse(player, appVersion, timeZone, response);

        return response;
    }

    private void addQuestsToResponse(Player player, Version appVersion, TimeZone timeZone, Response response) {
        response.completedHiddenQuestIds = questGateway.findCompletedHiddenQuestIds(player.getId());

        dailyQuestGenerator.tryToGenerateDailyQuest(player, appVersion, timeZone);
        response.dailyQuests = questGateway.findDailyQuests(player.getId());
    }

    private void addFoilCardsToResponse(Player player, Response response) {
        response.foilTowers = new ArrayList<>();
        response.foilItems = new ArrayList<>();
        response.foilPotions = new ArrayList<>();
        response.foilHeroes = new ArrayList<>();

        List<FoilCard> foilCards = foilCardGateway.getFoilCardsForPlayerId(player.getId());
        if (foilCards != null) {
            for (FoilCard foilCard : foilCards) {
                addFoilCardToResponse(foilCard, response);
            }
        }
    }

    private void addFoilCardToResponse(FoilCard foilCard, Response response) {
        Response.Card card = createCard(foilCard);

        switch (foilCard.getCardType()) {
            case CardType.TOWER: response.foilTowers.add(card);
            case CardType.ITEM: response.foilItems.add(card);
            case CardType.POTION: response.foilPotions.add(card);
            case CardType.HERO: response.foilHeroes.add(card);
        }
    }

    private Response.Card createCard(FoilCard foilCard) {
        Response.Card card = new Response.Card();
        card.id = foilCard.getCardId();
        card.amount = foilCard.getAmount();
        return card;
    }

    private void addPlayerToResponse(Player player, Response response) {
        response.id = player.getId();
        response.name = player.getName();
        response.level = player.getLevel();
        response.experience = player.getExperience();
        response.relics = player.getRelics();
    }

    public static class Request {
        public String key;
        public String appVersion;
        public int timeZoneOffset;
    }

    public static class Response {
        public long id;
        public String name;
        public int level;
        public long experience;
        public int relics;
        public List<Card> foilTowers;
        public List<Card> foilItems;
        public List<Card> foilPotions;
        public List<Card> foilHeroes;
        public List<Long> completedHiddenQuestIds;
        public List<Quest> dailyQuests;

        public static class Card {
            public long id;
            public int amount;
        }
    }
}