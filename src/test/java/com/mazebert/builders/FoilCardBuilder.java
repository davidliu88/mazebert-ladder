package com.mazebert.builders;

import com.mazebert.entities.*;
import org.jusecase.builders.Builder;

public class FoilCardBuilder implements Builder<FoilCard> {
    private FoilCard foilCard = new FoilCard();

    public FoilCardBuilder one() {
        return this.withAmount(1);
    }

    public FoilCardBuilder withCard(Card card) {
        return getBuilderForCard(card)
                .withCardId(card.getId());
    }

    private FoilCardBuilder getBuilderForCard(Card card) {
        if (card instanceof Tower) {
            return tower();
        } else if (card instanceof Potion) {
            return potion();
        } else if (card instanceof Item) {
            return item();
        } else if (card instanceof Hero) {
            return hero();
        }
        return this;
    }

    public FoilCardBuilder tower() {
        return this.one().withCardType(CardType.TOWER);
    }

    public FoilCardBuilder potion() {
        return this.one().withCardType(CardType.POTION);
    }

    public FoilCardBuilder item() {
        return this.one().withCardType(CardType.ITEM);
    }

    public FoilCardBuilder hero() {
        return this.one().withCardType(CardType.HERO);
    }

    public FoilCardBuilder bowlingBall() {
        return this
                .item()
                .withCardId(58);
    }

    @Override
    public FoilCard build() {
        return foilCard;
    }

    public FoilCardBuilder withCardId(long value) {
        foilCard.setCardId(value);
        return this;
    }

    public FoilCardBuilder withAmount(int value) {
        foilCard.setAmount(value);
        return this;
    }

    public FoilCardBuilder withCardType(int value) {
        foilCard.setCardType(value);
        return this;
    }
}
