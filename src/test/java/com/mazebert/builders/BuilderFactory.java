package com.mazebert.builders;

import com.mazebert.entities.Item;
import org.jusecase.builders.DateBuilder;

import static org.jusecase.builders.BuilderFactory.date;

public class BuilderFactory {
    public static DateBuilder dateAtWeekend() {
        return date().with("2016-02-13 10:00:00");
    }

    public static DateBuilder dateAtWeek() {
        return date().with("2016-02-15 10:00:00");
    }

    public static PlayerBuilder player() {
        return new PlayerBuilder();
    }

    public static PlayerRowBuilder playerRow() {
        return new PlayerRowBuilder();
    }

    public static FoilCardBuilder foilCard() {
        return new FoilCardBuilder();
    }

    public static QuestBuilder quest() {
        return new QuestBuilder();
    }

    public static PurchaseBuilder purchase() {
        return new PurchaseBuilder();
    }

    public static BlackMarketOfferBuilder blackMarketOffer() {
        return new BlackMarketOfferBuilder();
    }

    public static ItemBuilder item() {
        return new ItemBuilder();
    }
}
