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

    public TowerBuilder kiwiEgg() {
        return this
                .normalCard()
                .withId(39)
                .withName("Kiwi Egg")
                .withRarity(CardRarity.LEGENDARY)
                .withSinceVersion("1.3");
    }

    public TowerBuilder kiwi() {
        return this
                .normalCard()
                .withIsForgeable(false)
                .withId(40)
                .withName("Kiwi")
                .withRarity(CardRarity.LEGENDARY)
                .withSinceVersion("1.3");
    }

    public TowerBuilder bloodDemon() {
        return this
                .normalCard()
                .withId(35)
                .withName("Blood Demon")
                .withRarity(CardRarity.LEGENDARY)
                .withSinceVersion("1.0");
    }
}
