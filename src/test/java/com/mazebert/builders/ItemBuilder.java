package com.mazebert.builders;

import com.mazebert.entities.CardRarity;
import com.mazebert.entities.Item;

public class ItemBuilder extends CardBuilder<ItemBuilder, Item> {
    @Override
    protected Item createCard() {
        return new Item();
    }

    public ItemBuilder bowlingBall() {
        return this
                .blackMarketCard()
                .withId(58)
                .withName("The Dude")
                .withRarity(CardRarity.LEGENDARY)
                .withSinceVersion("1.2");
    }

    public ItemBuilder mjoelnir() {
        return this
                .blackMarketCard()
                .withId(63)
                .withName("Mjoelnir")
                .withRarity(CardRarity.LEGENDARY)
                .withSinceVersion("1.3");
    }

    public ItemBuilder seelenreisser() {
        return this
                .normalCard()
                .withId(53)
                .withName("Seelenreisser")
                .withRarity(CardRarity.LEGENDARY)
                .withSinceVersion("1.0");
    }

    public ItemBuilder babySword() {
        return this
                .normalCard()
                .withId(4)
                .withName("Baby Sword")
                .withRarity(CardRarity.COMMON)
                .withSinceVersion("0.2");
    }

    public ItemBuilder scepterOfTime() {
        return this
                .normalCard()
                .withId(29)
                .withName("Scepter of Time")
                .withRarity(CardRarity.UNIQUE)
                .withSinceVersion("0.8");
    }

    public ItemBuilder bloodDemonBlade() {
        return this
                .normalCard()
                .withIsForgeable(false)
                .withId(52)
                .withName("Blood Demon''s Blade")
                .withRarity(CardRarity.LEGENDARY)
                .withSinceVersion("1.0");
    }
}
