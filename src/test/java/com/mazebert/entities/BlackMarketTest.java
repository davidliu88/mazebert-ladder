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

import static com.mazebert.builders.BuilderFactory.blackMarketOffer;
import static com.mazebert.builders.BuilderFactory.item;
import static org.junit.Assert.*;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.date;
import static org.jusecase.Builders.list;

public class BlackMarketTest {
    private CurrentDatePluginMock currentDatePlugin = new CurrentDatePluginMock();
    private BlackMarketOfferGatewayMock offerGateway = new BlackMarketOfferGatewayMock();
    private BlackMarketSettingsGatewayMock blackMarketSettingsGateway = new BlackMarketSettingsGatewayMock();
    private CardGatewayMock cardGateway = new CardGatewayMock();
    private RandomNumberGeneratorMock randomNumberGenerator = new RandomNumberGeneratorMock();
    private BlackMarket blackMarket = new BlackMarket(currentDatePlugin,
            offerGateway, blackMarketSettingsGateway, cardGateway,
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
        offerGateway.thenOfferWasCreatedForCard(a(item().mjoelnir()));
    }

    @Test
    public void createOffer_expirationDateAfterWeekend_1() {
        cardGateway.givenCardExists(a(item().mjoelnir()));
        currentDatePlugin.givenCurrentDate(a(date().with("2015-09-25 19:23:01")));
        whenOfferIsCreated();
        offerGateway.thenOfferWasCreatedWithExpirationDate(a(date().with("2015-09-28 00:00:00")));
    }

    @Test
    public void createOffer_expirationDateAfterWeekend_2() {
        cardGateway.givenCardExists(a(item().mjoelnir()));
        currentDatePlugin.givenCurrentDate(a(date().with("2015-12-01 01:05:14")));
        whenOfferIsCreated();
        offerGateway.thenOfferWasCreatedWithExpirationDate(a(date().with("2015-12-07 00:00:00")));
    }

    @Test
    public void createOfferOnMonday() {
        cardGateway.givenCardExists(a(item().mjoelnir()));
        currentDatePlugin.givenCurrentDate(a(date().with("2016-03-21 12:00:14")));
        whenOfferIsCreated();
        offerGateway.thenOfferWasCreatedWithExpirationDate(a(date().with("2016-03-28 00:00:00")));
    }

    @Test
    public void createOffer_cardIsPickedRandomly() {
        randomNumberGenerator.givenRandomIntegers(1);
        cardGateway.givenCardsExist(a(list(
                a(item().babySword()),
                a(item().mjoelnir()),
                a(item().bowlingBall())
        )));
        whenOfferIsCreated();
        randomNumberGenerator.thenRandomIntegerCallsAre("min: 0, max: 1");
        thenOfferWasCreatedForCard(a(item().bowlingBall()));
    }

    @Test
    public void getOffer_currentOfferNotExpired() {
        cardGateway.givenCardExists(a(item().bowlingBall()));
        offerGateway.givenLatestOffer(a(blackMarketOffer()
                .withCard(item().mjoelnir())
                .withExpirationDate(a(date().with("2015-07-20 00:00:00")))
        ));
        currentDatePlugin.givenCurrentDate(a(date().with("2015-07-19 01:05:14")));

        whenOfferIsGet();

        offerGateway.thenNoOfferWasCreated();
    }

    @Test
    public void getOffer_currentOfferIsExpired() {
        cardGateway.givenCardExists(a(item().bowlingBall()));
        offerGateway.givenLatestOffer(a(blackMarketOffer()
                .withCard(item().mjoelnir())
                .withExpirationDate(a(date().with("2015-07-20 00:00:00")))
        ));
        currentDatePlugin.givenCurrentDate(a(date().with("2015-07-25 10:01:14")));

        whenOfferIsGet();

        offerGateway.thenOfferWasCreatedForCard(a(item().bowlingBall()));
    }

    @Test
    public void getOffer_currentOfferNotExpired_midnight() {
        cardGateway.givenCardExists(a(item().bowlingBall()));
        offerGateway.givenLatestOffer(a(blackMarketOffer()
                .withCard(item().mjoelnir())
                .withExpirationDate(a(date().with("2015-07-20 00:00:00")))
        ));
        currentDatePlugin.givenCurrentDate(a(date().with("2015-07-20 00:00:00")));

        whenOfferIsGet();

        offerGateway.thenNoOfferWasCreated();
    }

    @Test
    public void getOffer_currentOfferNotExpired_midnight_maximumTimeOffsetIsRespected() {
        cardGateway.givenCardExists(a(item().bowlingBall()));
        offerGateway.givenLatestOffer(a(blackMarketOffer()
                .withCard(item().mjoelnir())
                .withExpirationDate(a(date().with("2015-07-20 00:00:00")))
        ));
        currentDatePlugin.givenCurrentDate(a(date().with("2015-07-20 12:00:00")));

        whenOfferIsGet();

        offerGateway.thenNoOfferWasCreated();
    }

    @Test
    public void getOffer_currentOfferNotExpired_oneSecondPastMidnight_maximumTimeOffsetIsRespected() {
        cardGateway.givenCardExists(a(item().bowlingBall()));
        offerGateway.givenLatestOffer(a(blackMarketOffer()
                .withCard(item().mjoelnir())
                .withExpirationDate(a(date().with("2015-07-20 00:00:00")))
        ));
        currentDatePlugin.givenCurrentDate(a(date().with("2015-07-20 12:00:01")));

        whenOfferIsGet();

        offerGateway.thenOfferWasCreatedForCard(a(item().bowlingBall()));
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

    private void whenOfferIsGet() {
        offer = blackMarket.getOffer();
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