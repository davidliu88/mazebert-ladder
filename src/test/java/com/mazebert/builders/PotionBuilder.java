package com.mazebert.builders;

import com.mazebert.entities.CardRarity;
import com.mazebert.entities.Potion;

public class PotionBuilder extends CardBuilder<PotionBuilder, Potion> {
    @Override
    protected Potion createCard() {
        return new Potion();
    }

    public PotionBuilder tearsOfTheGods() {
        return this
                .normalCard()
                .withId(13)
                .withName("Tears of the Gods")
                .withRarity(CardRarity.UNIQUE)
                .withSinceVersion("0.4");
    }

    public PotionBuilder angelicElixir() {
        return this
                .supporterCard()
                .withId(19)
                .withName("Angelic Elixir")
                .withRarity(CardRarity.LEGENDARY)
                .withSinceVersion("1.0");
    }
}
