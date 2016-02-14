package com.mazebert.entities;

public class Item extends Card {
    @Override
    public int getType() {
        return CardType.ITEM;
    }
}
