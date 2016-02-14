package com.mazebert.entities;

import com.mazebert.gateways.mocks.BlackMarketOfferGatewayMock;
import com.mazebert.gateways.mocks.BlackMarketSettingsGatewayMock;
import com.mazebert.gateways.mocks.CardGatewayMock;
import com.mazebert.plugins.random.mocks.RandomNumberGeneratorMock;
import com.mazebert.plugins.time.TimeZoneParser;
import com.mazebert.plugins.time.mocks.CurrentDatePluginMock;
import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

import static com.mazebert.builders.BuilderFactory.item;
import static org.junit.Assert.*;
import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.date;
import static org.jusecase.builders.BuilderFactory.listWith;

public class BlackMarketTest {
    private CurrentDatePluginMock currentDatePlugin = new CurrentDatePluginMock();
    private BlackMarketOfferGatewayMock blackMarketOfferGateway = new BlackMarketOfferGatewayMock();
    private BlackMarketSettingsGatewayMock blackMarketSettingsGateway = new BlackMarketSettingsGatewayMock();
    private CardGatewayMock cardGateway = new CardGatewayMock();
    private RandomNumberGeneratorMock randomNumberGenerator = new RandomNumberGeneratorMock();
    private BlackMarket blackMarket = new BlackMarket(currentDatePlugin,
            blackMarketOfferGateway, blackMarketSettingsGateway, cardGateway,
            randomNumberGenerator);

    private TimeZone timeZone;
    private BlackMarketOffer offer;

    @Before
    public void setUp() throws Exception {
        givenTimeZoneOffset(0);
    }

    @Test
    public void isAvailable_startOfWeekend() {
        givenCurrentDate("2015-09-18 18:00:00");
        thenBlackMarketIsAvailable();
    }

    @Test
    public void isAvailable_middleOfWeekend() {
        givenCurrentDate("2015-09-19 21:30:41");
        thenBlackMarketIsAvailable();
    }

    @Test
    public void isAvailable_endOfWeekend() {
        givenCurrentDate("2015-09-20 23:59:59");
        thenBlackMarketIsAvailable();
    }

    @Test
    public void isAvailable_startOfWeek() {
        givenCurrentDate("2015-09-21 00:00:00");
        thenBlackMarketIsNotAvailable();
    }

    @Test
    public void isAvailable_endOfWeek() {
        givenCurrentDate("2015-09-25 17:59:59");
        thenBlackMarketIsNotAvailable();
    }

    @Test
    public void isAvailable_startOfWeekendBecauseTimeZoneOffset() {
        givenTimeZoneOffset(1);
        givenCurrentDate("2015-09-25 17:00:00");
        thenBlackMarketIsAvailable();
    }

    @Test
    public void isAvailable_endOfWeekendBecauseTimeZoneOffset() {
        givenTimeZoneOffset(-5);
        givenCurrentDate("2015-09-21 04:59:59");
        thenBlackMarketIsAvailable();
    }

    @Test
    public void createOffer_noAvailableCards() {
        cardGateway.givenNoCardsExist();
        whenOfferIsCreated();
        thenOfferIsNull();
    }

    @Test
    public void createOffer_noAvailableBlackMarketCards() {
        cardGateway.givenCardExists(a(item().babySword()));
        whenOfferIsCreated();
        thenOfferIsNull();
    }

    @Test
    public void createOffer_oneAvailableBlackMarketCard() {
        cardGateway.givenCardExists(a(item().mjoelnir()));
        whenOfferIsCreated();
        thenOfferWasCreatedForCard(a(item().mjoelnir()));
    }

    @Test
    public void createOffer_offerIsPersisted() {
        cardGateway.givenCardExists(a(item().mjoelnir()));
        whenOfferIsCreated();
        blackMarketOfferGateway.thenOfferWasCreatedForCard(a(item().mjoelnir()));
    }

    @Test
    public void createOffer_expirationDateAfterWeekend_1() {
        cardGateway.givenCardExists(a(item().mjoelnir()));
        currentDatePlugin.givenCurrentDate(a(date().with("2015-09-25 19:23:01")));
        whenOfferIsCreated();
        blackMarketOfferGateway.thenOfferWasCreatedWithExpirationDate(a(date().with("2015-09-28 00:00:00")));
    }

    @Test
    public void createOffer_expirationDateAfterWeekend_2() {
        cardGateway.givenCardExists(a(item().mjoelnir()));
        currentDatePlugin.givenCurrentDate(a(date().with("2015-12-01 01:05:14")));
        whenOfferIsCreated();
        blackMarketOfferGateway.thenOfferWasCreatedWithExpirationDate(a(date().with("2015-12-07 00:00:00")));
    }

    @Test
    public void createOffer_cardIsPickedRandomly() {
        randomNumberGenerator.givenRandomIntegers(1);
        cardGateway.givenCardsExist(a(listWith(
                a(item().babySword()),
                a(item().mjoelnir()),
                a(item().bowlingBall())
        )));
        whenOfferIsCreated();
        randomNumberGenerator.thenRandomIntegerCallsAre("min: 0, max: 1");
        thenOfferWasCreatedForCard(a(item().bowlingBall()));
    }

    private void givenTimeZoneOffset(int offset) {
        timeZone = new TimeZoneParser().parseOffset(offset);
    }

    private void givenCurrentDate(String date) {
        currentDatePlugin.givenCurrentDate(a(date().with(date)));
    }

    private void whenOfferIsCreated() {
        offer = blackMarket.createOffer();
    }

    private void thenBlackMarketIsAvailable() {
        assertTrue(blackMarket.isAvailable(timeZone));
    }

    private void thenBlackMarketIsNotAvailable() {
        assertFalse(blackMarket.isAvailable(timeZone));
    }

    private void thenOfferIsNull() {
        assertNull(offer);
    }

    private void thenOfferWasCreatedForCard(Card card) {
        assertEquals(card.getId(), offer.getCardId());
        assertEquals(card.getType(), offer.getCardType());
    }
}