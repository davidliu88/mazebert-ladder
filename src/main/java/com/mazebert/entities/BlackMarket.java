package com.mazebert.entities;

import com.mazebert.gateways.BlackMarketOfferGateway;
import com.mazebert.gateways.CardGateway;
import com.mazebert.plugins.time.CurrentDatePlugin;

import java.util.Calendar;
import java.util.TimeZone;

public class BlackMarket {
    private final CurrentDatePlugin currentDatePlugin;
    private final BlackMarketOfferGateway blackMarketOfferGateway;
    private final CardGateway cardGateway;

    public BlackMarket(CurrentDatePlugin currentDatePlugin,
                       BlackMarketOfferGateway blackMarketOfferGateway,
                       CardGateway cardGateway) {
        this.currentDatePlugin = currentDatePlugin;
        this.blackMarketOfferGateway = blackMarketOfferGateway;
        this.cardGateway = cardGateway;
    }

    public boolean isAvailable(Version appVersion, TimeZone timeZone) {
        BlackMarketOffer offer = getOffer();

        Card card = cardGateway.findCard(offer.getCardId(), offer.getCardType());
        if (card != null) {
            Version offerVersion = new Version(card.getSinceVersion());
            if (appVersion.compareTo(offerVersion) >= 0) {
                return isAvailable(timeZone);
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

            blackMarketOfferGateway.addOffer(offer);
        }
        return offer;
    }

    private BlackMarketOffer createOffer() {
        BlackMarketOffer offer = new BlackMarketOffer();
        offer.setCardId(58);
        offer.setCardType(CardType.ITEM);
        return offer;
    }
}
