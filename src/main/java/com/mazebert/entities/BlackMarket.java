package com.mazebert.entities;

import com.mazebert.gateways.BlackMarketOfferGateway;
import com.mazebert.gateways.BlackMarketSettingsGateway;
import com.mazebert.gateways.CardGateway;
import com.mazebert.plugins.random.RandomNumberGenerator;
import com.mazebert.plugins.time.CurrentDatePlugin;

import java.util.*;

public class BlackMarket {
    private static final int DEFAULT_PRICE = 250;

    private final CurrentDatePlugin currentDatePlugin;
    private final BlackMarketOfferGateway offerGateway;
    private final BlackMarketSettingsGateway settingsGateway;
    private final CardGateway cardGateway;
    private final RandomNumberGenerator randomNumberGenerator;

    public BlackMarket(CurrentDatePlugin currentDatePlugin,
                       BlackMarketOfferGateway offerGateway,
                       BlackMarketSettingsGateway settingsGateway,
                       CardGateway cardGateway,
                       RandomNumberGenerator randomNumberGenerator) {
        this.currentDatePlugin = currentDatePlugin;
        this.offerGateway = offerGateway;
        this.settingsGateway = settingsGateway;
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
        BlackMarketOffer offer = offerGateway.findLatestOffer();
        if (offer == null || isOfferExpired(offer)) {
            offer = createOffer();
        }
        return offer;
    }

    private boolean isOfferExpired(BlackMarketOffer offer) {
        if (offer.getExpirationDate() == null) {
            return false;
        }

        long now = currentDatePlugin.getCurrentDate().getTime();
        long expiration = offer.getExpirationDate().getTime() + 12 * 3600000;

        return now > expiration;
    }

    public BlackMarketOffer createOffer() {
        Card card = getRandomCard();
        return card == null ? null : createOfferFromCard(card);
    }

    private BlackMarketOffer createOfferFromCard(Card card) {
        BlackMarketOffer offer = new BlackMarketOffer();
        offer.setCardId(card.getId());
        offer.setCardType(card.getType());
        offer.setExpirationDate(determineExpirationDate());
        offerGateway.addOffer(offer);
        return offer;
    }

    private Card getRandomCard() {
        List<Card> cards = cardGateway.findAllBlackMarketCards();
        if (cards.isEmpty()) {
            return null;
        }

        return cards.get(randomNumberGenerator.randomInteger(0, cards.size() - 1));
    }

    private Date determineExpirationDate() {
        Calendar calendar = Calendar.getInstance(new SimpleTimeZone(0, "utc"));
        calendar.setTime(currentDatePlugin.getCurrentDate());
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public int getPrice() {
        BlackMarketSettings settings = settingsGateway.getSettings();
        return settings != null ? settings.getPrice() : DEFAULT_PRICE;
    }

    public BlackMarketOffer getThisWeeksPurchase(Player player) {
        BlackMarketOffer offer = getOffer();
        if (offerGateway.isOfferPurchased(offer, player)) {
            return offer;
        }
        return null;
    }
}
