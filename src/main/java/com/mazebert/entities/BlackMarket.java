package com.mazebert.entities;

import com.mazebert.gateways.BlackMarketOfferGateway;
import com.mazebert.gateways.CardGateway;
import com.mazebert.plugins.random.RandomNumberGenerator;
import com.mazebert.plugins.time.CurrentDatePlugin;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class BlackMarket {
    private final CurrentDatePlugin currentDatePlugin;
    private final BlackMarketOfferGateway blackMarketOfferGateway;
    private final CardGateway cardGateway;
    private final RandomNumberGenerator randomNumberGenerator;

    public BlackMarket(CurrentDatePlugin currentDatePlugin,
                       BlackMarketOfferGateway blackMarketOfferGateway,
                       CardGateway cardGateway,
                       RandomNumberGenerator randomNumberGenerator) {
        this.currentDatePlugin = currentDatePlugin;
        this.blackMarketOfferGateway = blackMarketOfferGateway;
        this.cardGateway = cardGateway;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public boolean isAvailable(Version appVersion, TimeZone timeZone) {
        BlackMarketOffer offer = getOffer();
        if (offer != null) {
            Card card = cardGateway.findCard(offer.getCardId(), offer.getCardType());
            if (card != null) {
                Version offerVersion = new Version(card.getSinceVersion());
                if (appVersion.compareTo(offerVersion) >= 0) {
                    return isAvailable(timeZone);
                }
            }
        }

        return false;
    }

    public boolean isAvailable(TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(currentDatePlugin.getCurrentDate());
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
            return true;
        }

        if (day == Calendar.FRIDAY) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            return hour >= 18;
        }

        return false;
    }

    public BlackMarketOffer getOffer() {
        BlackMarketOffer offer = blackMarketOfferGateway.findLatestOffer();
        if (offer == null) {
            offer = createOffer();
        }
        return offer;
    }

    public BlackMarketOffer createOffer() {
        Card card = getRandomCard();
        return card == null ? null : createOfferFromCard(card);
    }

    private BlackMarketOffer createOfferFromCard(Card card) {
        BlackMarketOffer offer = new BlackMarketOffer();
        offer.setCardId(card.getId());
        offer.setCardType(card.getType());
        blackMarketOfferGateway.addOffer(offer);
        return offer;
    }

    private Card getRandomCard() {
        List<Card> cards = cardGateway.findAllBlackMarketCards();
        if (cards.isEmpty()) {
            return null;
        }

        return cards.get(randomNumberGenerator.randomInteger(0, cards.size() - 1));
    }
}
