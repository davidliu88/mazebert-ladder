package com.mazebert.builders;

import com.mazebert.entities.Card;
import org.jusecase.builders.Builder;

@SuppressWarnings("unchecked")
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

    public BuilderType normalCard() {
        return (BuilderType) this
                .withIsForgeable(true)
                .withIsBlackMarketOffer(false);
    }

    public BuilderType blackMarketCard() {
        return (BuilderType) this
                .withIsForgeable(false)
                .withIsBlackMarketOffer(true);
    }

    public BuilderType supporterCard() {
        return (BuilderType) this
                .withIsForgeable(false)
                .withIsBlackMarketOffer(false);
    }

    public BuilderType withId(long value) {
        card.setId(value);
        return (BuilderType) this;
    }

    public BuilderType withName(String value) {
        card.setName(value);
        return (BuilderType) this;
    }

    public BuilderType withSinceVersion(String value) {
        card.setSinceVersion(value);
        return (BuilderType) this;
    }

    public BuilderType withRarity(int value) {
        card.setRarity(value);
        return (BuilderType) this;
    }

    public BuilderType withIsBlackMarketOffer(boolean value) {
        card.setBlackMarketOffer(value);
        return (BuilderType) this;
    }

    public BuilderType withIsForgeable(boolean value) {
        card.setForgeable(value);
        return (BuilderType) this;
    }
}
