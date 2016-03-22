package com.mazebert.entities;

import com.mazebert.builders.TowerBuilder;
import org.junit.Test;

import static com.mazebert.builders.BuilderFactory.tower;
import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;

public class CardTest {
    @Test
    public void getTradeValue_unknownRarity() {
        thenTradeValueIs(0, 0);
        thenTradeValueIs(100, 0);
        thenTradeValueIs(-1, 0);
    }

    @Test
    public void getTradeValue() {
        thenTradeValueIs(CardRarity.COMMON, 2);
        thenTradeValueIs(CardRarity.UNCOMMON, 4);
        thenTradeValueIs(CardRarity.RARE, 8);
        thenTradeValueIs(CardRarity.UNIQUE, 16);
        thenTradeValueIs(CardRarity.LEGENDARY, 40);
    }

    @Test
    public void getPrice_unknownRarity() {
        thenPriceIs(0, 0);
        thenPriceIs(100, 0);
        thenPriceIs(-1, 0);
    }

    @Test
    public void getPrice() {
        thenPriceIs(CardRarity.COMMON, 50);
        thenPriceIs(CardRarity.UNCOMMON, 100);
        thenPriceIs(CardRarity.RARE, 150);
        thenPriceIs(CardRarity.UNIQUE, 200);
        thenPriceIs(CardRarity.LEGENDARY, 400);
    }

    private void thenTradeValueIs(int rarity, int expectedValue) {
        assertEquals(expectedValue, a(card().withRarity(rarity)).getTradeValue());
    }

    private void thenPriceIs(int rarity, int expectedPrice) {
        assertEquals(expectedPrice, a(card().withRarity(rarity)).getPrice());
    }

    private TowerBuilder card() {
        return tower();
    }
}