package com.mazebert.builders;

import com.mazebert.entities.CardRarity;
import com.mazebert.entities.Tower;

public class TowerBuilder extends CardBuilder<TowerBuilder, Tower> {
    @Override
    protected Tower createCard() {
        return new Tower();
    }

    public TowerBuilder huliTheMonkey() {
        return this
                .normalCard()
                .withId(7)
                .withName("Huli the Monkey")
                .withRarity(CardRarity.RARE)
                .withSinceVersion("0.1");
    }

    public TowerBuilder herbWitch() {
        return this
                .normalCard()
                .withId(5)
                .withName("Herb Witch")
                .withRarity(CardRarity.UNCOMMON)
                .withSinceVersion("0.1");
    }
}
