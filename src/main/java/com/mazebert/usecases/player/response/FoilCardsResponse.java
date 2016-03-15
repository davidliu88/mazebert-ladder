package com.mazebert.usecases.player.response;

import com.mazebert.entities.CardType;
import com.mazebert.entities.FoilCard;
import com.mazebert.entities.Player;
import com.mazebert.gateways.FoilCardGateway;
import com.mazebert.usecases.player.SynchronizePlayer;
import org.jusecase.builders.Builder;

import java.util.ArrayList;
import java.util.List;

public class FoilCardsResponse {
    public List<Card> foilTowers;
    public List<Card> foilItems;
    public List<Card> foilPotions;
    public List<Card> foilHeroes;

    public void addFoilCards(Player player, FoilCardGateway foilCardGateway) {
        foilTowers = new ArrayList<>();
        foilItems = new ArrayList<>();
        foilPotions = new ArrayList<>();
        foilHeroes = new ArrayList<>();

        List<FoilCard> foilCards = foilCardGateway.getFoilCardsForPlayerId(player.getId());
        if (foilCards != null) {
            for (FoilCard foilCard : foilCards) {
                addFoilCardToResponse(foilCard);
            }
        }
    }

    private void addFoilCardToResponse(FoilCard foilCard) {
        Card card = createCard(foilCard);

        switch (foilCard.getCardType()) {
            case CardType.TOWER:
                foilTowers.add(card);
                break;
            case CardType.ITEM:
                foilItems.add(card);
                break;
            case CardType.POTION:
                foilPotions.add(card);
                break;
            case CardType.HERO:
                foilHeroes.add(card);
                break;
        }
    }

    private Card createCard(FoilCard foilCard) {
        Card card = new Card();
        card.id = foilCard.getCardId();
        card.amount = foilCard.getAmount();
        return card;
    }

    public static class Card {
        public long id;
        public int amount;
    }

    public static class CardBuilder implements Builder<Card> {
        private SynchronizePlayer.Response.Card card = new Card();

        public CardBuilder withId(long value) {
            card.id = value;
            return this;
        }

        public CardBuilder withAmount(int value) {
            card.amount = value;
            return this;
        }

        @Override
        public Card build() {
            return card;
        }
    }
}
