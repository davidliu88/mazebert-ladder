package com.mazebert.plugins.time;

import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class TimeZoneParser {
    private static final int ONE_HOUR_MILLIS = 60 * 60 * 1000;

    public TimeZone parseOffset(int timeZoneOffset) {
        if (timeZoneOffset < -12) {
            timeZoneOffset = -12;
        }

        if (timeZoneOffset > 12) {
            timeZoneOffset = 12;
        }

        return new SimpleTimeZone(timeZoneOffset * ONE_HOUR_MILLIS, "ParsedTimeZone");
    }

    /**
     * Note: The processing is necessary because:
     * AIR Client sends difference of UTC - LOCAL
     * Server needs difference of LOCAL - UTC
     */
    public TimeZone parseAppOffset(int timeZoneOffset) {
        return parseOffset(-timeZoneOffset);
    }
}
