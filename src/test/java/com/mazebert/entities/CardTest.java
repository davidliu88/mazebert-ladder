package com.mazebert.entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CardTest {
    @Test
    public void getTradeValue_unknownRarity() {
        Card card = new Card() {
            @Override
            public int getType() {
                return 0;
            }
        };
        card.setRarity(0);

        assertEquals(0, card.getTradeValue());
    }
}