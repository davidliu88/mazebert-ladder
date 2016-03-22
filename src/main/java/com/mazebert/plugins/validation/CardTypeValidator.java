package com.mazebert.plugins.validation;

import com.mazebert.error.BadRequest;

public class CardTypeValidator {
    public void validate(int cardType) {
        if (cardType <= 0 || cardType >= 5) {
            throw new BadRequest("This card type does not exist.");
        }
    }
}
