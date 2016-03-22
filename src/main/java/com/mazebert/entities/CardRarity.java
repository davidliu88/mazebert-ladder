package com.mazebert.entities;

public class CardRarity {
    public static final int COMMON = 1;
    public static final int UNCOMMON = 2;
    public static final int RARE = 3;
    public static final int UNIQUE = 4;
    public static final int LEGENDARY = 5;

    public static final int COUNT = 5;

    public static int getPrice(int rarity) {
        switch (rarity) {
            case CardRarity.COMMON:
                return 50;
            case CardRarity.UNCOMMON:
                return 100;
            case CardRarity.RARE:
                return 150;
            case CardRarity.UNIQUE:
                return 200;
            case CardRarity.LEGENDARY:
                return 400;
        }
        return 0;
    }
}
