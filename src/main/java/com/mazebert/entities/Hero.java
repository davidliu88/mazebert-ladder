package com.mazebert.entities;

public class Hero extends Card {
    @Override
    public int getType() {
        return CardType.HERO;
    }
}
