package com.mazebert.builders;

import com.mazebert.entities.BlackMarketOffer;
import com.mazebert.entities.Card;
import org.jusecase.builders.Builder;

import java.util.Date;

public class BlackMarketOfferBuilder implements Builder<BlackMarketOffer> {
    private BlackMarketOffer blackMarketOffer = new BlackMarketOffer();

    @Override
    public BlackMarketOffer build() {
        return blackMarketOffer;
    }

    public BlackMarketOfferBuilder withCard(Builder<? extends Card> builder) {
        return this.withCard(builder.build());
    }

    public BlackMarketOfferBuilder withCard(Card card) {
        return this
                .withCardId(card.getId())
                .withCardType(card.getType());
    }

    public BlackMarketOfferBuilder withCardType(int type) {
        blackMarketOffer.setCardType(type);
        return this;
    }

    public BlackMarketOfferBuilder withCardId(long id) {
        blackMarketOffer.setCardId(id);
        return this;
    }

    public BlackMarketOfferBuilder withId(long id) {
        blackMarketOffer.setId(id);
        return this;
    }

    public BlackMarketOfferBuilder withExpirationDate(Date date) {
        blackMarketOffer.setExpirationDate(date);
        return this;
    }
}
