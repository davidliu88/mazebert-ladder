package com.mazebert.builders;

import com.mazebert.entities.CardType;
import com.mazebert.entities.FoilCard;
import org.jusecase.builders.Builder;

public class FoilCardBuilder implements Builder<FoilCard> {
    private FoilCard foilCard = new FoilCard();

    public FoilCardBuilder tower() {
        return this.withCardType(CardType.TOWER);
    }

    public FoilCardBuilder potion() {
        return this.withCardType(CardType.POTION);
    }

    public FoilCardBuilder item() {
        return this.withCardType(CardType.ITEM);
    }

    public FoilCardBuilder hero() {
        return this.withCardType(CardType.HERO);
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
