package com.mazebert.builders;

public class BuilderFactory {
    public static PlayerBuilder player() {
        return new PlayerBuilder();
    }
}
