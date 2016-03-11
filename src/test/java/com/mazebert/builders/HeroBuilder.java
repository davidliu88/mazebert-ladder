package com.mazebert.builders;

import com.mazebert.entities.CardRarity;
import com.mazebert.entities.Hero;

public class HeroBuilder extends CardBuilder<HeroBuilder, Hero> {
    @Override
    protected Hero createCard() {
        return new Hero();
    }

    public HeroBuilder littlefinger() {
        return this
                .normalCard()
                .withId(1)
                .withName("Sir Littlefinger")
                .withRarity(CardRarity.COMMON)
                .withSinceVersion("1.0");
    }

    public HeroBuilder cookieMonster() {
        return this
                .supporterCard()
                .withId(5)
                .withName("Cookiemonster")
                .withRarity(CardRarity.LEGENDARY)
                .withSinceVersion("1.0");
    }

    public HeroBuilder innKeeper() {
        return this
                .supporterCard()
                .withId(6)
                .withName("Innkeeper")
                .withRarity(CardRarity.LEGENDARY)
                .withSinceVersion("1.0");
    }
}
