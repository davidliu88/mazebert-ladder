package com.mazebert.builders;

public class BuilderFactory {
    public static PlayerBuilder player() {
        return new PlayerBuilder();
    }

    public static PlayerRowBuilder playerRow() {
        return new PlayerRowBuilder();
    }

    public static FoilCardBuilder foilCard() {
        return new FoilCardBuilder();
    }
}
