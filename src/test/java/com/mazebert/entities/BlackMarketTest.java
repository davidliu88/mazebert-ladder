package com.mazebert.entities;

import com.mazebert.gateways.mocks.BlackMarketOfferGatewayMock;
import com.mazebert.gateways.mocks.CardGatewayMock;
import com.mazebert.plugins.time.TimeZoneParser;
import com.mazebert.plugins.time.mocks.CurrentDatePluginMock;
import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.jusecase.builders.BuilderFactory.a;
import static org.jusecase.builders.BuilderFactory.date;

public class BlackMarketTest {
    private CurrentDatePluginMock currentDatePlugin = new CurrentDatePluginMock();
    private BlackMarketOfferGatewayMock blackMarketOfferGateway = new BlackMarketOfferGatewayMock();
    private CardGatewayMock cardGateway = new CardGatewayMock();
    private BlackMarket blackMarket = new BlackMarket(currentDatePlugin, blackMarketOfferGateway, cardGateway);

    private TimeZone timeZone;

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

    private void givenTimeZoneOffset(int offset) {
        timeZone = new TimeZoneParser().parseOffset(offset);
    }

    private void givenCurrentDate(String date) {
        currentDatePlugin.givenCurrentDate(a(date().with(date)));
    }

    private void thenBlackMarketIsAvailable() {
        assertTrue(blackMarket.isAvailable(timeZone));
    }

    private void thenBlackMarketIsNotAvailable() {
        assertFalse(blackMarket.isAvailable(timeZone));
    }
}