package com.mazebert.builders;

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
                .withSinceVersion("1.2");
    }

    public ItemBuilder mjoelnir() {
        return this
                .blackMarketCard()
                .withId(63)
                .withName("Mjoelnir")
                .withSinceVersion("1.3");
    }

    public ItemBuilder babySword() {
        return this
                .normalCard()
                .withId(4)
                .withName("Baby Sword")
                .withSinceVersion("0.2");
    }
}
