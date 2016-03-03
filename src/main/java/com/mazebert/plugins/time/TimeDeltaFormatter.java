package com.mazebert.plugins.time;

import java.util.Date;

public class TimeDeltaFormatter {
    private final long nowThresholdInMilliseconds;
    private final String nowValue;

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_MONTH = 2592000000L;
    private static final long ONE_YEAR = 31536000000L;

    public TimeDeltaFormatter(long nowThresholdInSeconds, String nowValue) {
        this.nowThresholdInMilliseconds = nowThresholdInSeconds * 1000;
        this.nowValue = nowValue;
    }

    public String format(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return "";
        }

        long delta = date2.getTime() - date1.getTime();
        if (delta <= nowThresholdInMilliseconds) {
            return nowValue;
        }

        if (delta < ONE_HOUR) return formatMinutes(delta);
        if (delta < ONE_DAY) return formatHours(delta);
        if (delta < ONE_MONTH) return formatDays(delta);
        if (delta < ONE_YEAR) return formatMonths(delta);
        return formatYears(delta);
    }

    private String formatYears(long delta) {
        return formatTimeValue(delta / ONE_YEAR, "year");
    }

    private String formatMonths(long delta) {
        return formatTimeValue(delta / ONE_MONTH, "month");
    }

    private String formatDays(long delta) {
        return formatTimeValue(delta / ONE_DAY, "day");
    }

    private String formatHours(long delta) {
        return formatTimeValue(delta / ONE_HOUR, "hour");
    }

    private String formatMinutes(long delta) {
        return formatTimeValue(delta / ONE_MINUTE, "minute");
    }

    private String formatTimeValue(long value, String unit) {
        if (value > 1) {
            unit += "s";
        }
        return value + " " + unit;
    }
}
