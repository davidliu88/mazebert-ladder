package com.mazebert.builders;

import com.mazebert.entities.Item;

public class ItemBuilder extends CardBuilder<ItemBuilder, Item> {
    @Override
    protected Item createCard() {
        return new Item();
    }

    public ItemBuilder bowlingBall() {
        return this
                .withId(58)
                .withName("The Dude")
                .withSinceVersion("1.2");
    }
}
