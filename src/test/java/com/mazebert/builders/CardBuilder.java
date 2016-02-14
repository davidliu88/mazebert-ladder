package com.mazebert.builders;

import com.mazebert.entities.Card;
import org.jusecase.builders.Builder;

public abstract class CardBuilder<BuilderType extends CardBuilder, CardType extends Card> implements Builder<CardType> {
    protected CardType card;

    public CardBuilder() {
        card = createCard();
    }

    protected abstract CardType createCard();

    @Override
    public CardType build() {
        return card;
    }

    @SuppressWarnings("unchecked")
    public BuilderType withId(long value) {
        card.setId(value);
        return (BuilderType) this;
    }

    @SuppressWarnings("unchecked")
    public BuilderType withName(String value) {
        card.setName(value);
        return (BuilderType) this;
    }

    @SuppressWarnings("unchecked")
    public BuilderType withSinceVersion(String value) {
        card.setSinceVersion(value);
        return (BuilderType) this;
    }
}
