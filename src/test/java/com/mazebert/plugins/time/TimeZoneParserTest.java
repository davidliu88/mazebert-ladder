package com.mazebert.plugins.time;

import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class TimeZoneParserTest {
    private TimeZoneParser parser;

    private TimeZone timeZone;

    private static final int ONE_HOUR_MILLIS = 60 * 60 * 1000;

    @Before
    public void setUp() throws Exception {
        parser = new TimeZoneParser();
    }

    @Test
    public void appOffsetIsInverted() {
        for (int offset = -12; offset <= 12; ++offset) {
            timeZone = parser.parseAppOffset(offset);
            thenParsedOffsetIs(-offset);
        }
    }

    @Test
    public void positiveAppOffsetIsCropped() {
        timeZone = parser.parseAppOffset(13);
        thenParsedOffsetIs(-12);
    }

    @Test
    public void negativeAppOffsetIsCropped() {
        timeZone = parser.parseAppOffset(-13);
        thenParsedOffsetIs(12);
    }

    private void thenParsedOffsetIs(int expected) {
        assertEquals(expected * ONE_HOUR_MILLIS, timeZone.getRawOffset());
    }
}