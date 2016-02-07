package com.mazebert.usecases.player;

import com.mazebert.entities.CardType;
import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.gateways.PlayerGateway;
import org.jusecase.Usecase;

import java.util.ArrayList;
import java.util.List;

public class SynchronizePlayer implements Usecase<SynchronizePlayer.Request, SynchronizePlayer.Response> {
    private final PlayerGateway playerGateway;
    private final FoilCardGateway foilCardGateway;

    public SynchronizePlayer(PlayerGateway playerGateway, FoilCardGateway foilCardGateway) {
        this.playerGateway = playerGateway;
        this.foilCardGateway = foilCardGateway;
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

        Response response = new Response();
        addPlayerToResponse(player, response);
        addFoilCardsToResponse(player, response);

        return response;
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

        public static class Card {
            public long id;
            public int amount;
        }
    }
}