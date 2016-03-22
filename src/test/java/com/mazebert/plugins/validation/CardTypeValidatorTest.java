package com.mazebert.plugins.validation;

import com.mazebert.entities.CardType;
import com.mazebert.error.BadRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CardTypeValidatorTest {
    private CardTypeValidator cardTypeValidator = new CardTypeValidator();

    @Test
    public void validCardTypes() {
        thenCardTypeIsValid(CardType.TOWER);
        thenCardTypeIsValid(CardType.ITEM);
        thenCardTypeIsValid(CardType.POTION);
        thenCardTypeIsValid(CardType.HERO);
    }

    @Test
    public void invalidCardTypes() {
        thenCardTypeIsInvalid(0);
        thenCardTypeIsInvalid(5);
        thenCardTypeIsInvalid(100);
    }

    private void thenCardTypeIsValid(int cardType) {
        cardTypeValidator.validate(cardType);
    }

    private void thenCardTypeIsInvalid(int cardType) {
        BadRequest error = null;
        try {
            cardTypeValidator.validate(cardType);
        } catch (BadRequest e) {
            error = e;
        }

        assertNotNull("Expected that card type " + cardType + " is not valid.", error);
        assertEquals("This card type does not exist.", error.getMessage());
    }
}