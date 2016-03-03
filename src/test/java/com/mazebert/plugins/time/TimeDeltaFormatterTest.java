package com.mazebert.plugins.time;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.jusecase.Builders.a;
import static org.jusecase.Builders.date;

public class TimeDeltaFormatterTest {
    private TimeDeltaFormatter formatter = new TimeDeltaFormatter(600, "now");

    private Date date1;
    private Date date2;

    @Test
    public void nullDates() {
        givenDate1(null);
        givenDate2(null);
        thenResultIs("");
    }

    @Test
    public void nullLastUpdate() {
        givenDate1(null);
        givenDate2("2016-01-19 23:00:00");
        thenResultIs("");
    }

    @Test
    public void nullCurrentDate() {
        givenDate1("2016-01-19 23:00:00");
        givenDate2(null);
        thenResultIs("");
    }

    @Test
    public void sameTimes() {
        givenDate1("2016-01-19 23:00:00");
        givenDate2("2016-01-19 23:00:00");
        thenResultIs("now");
    }

    @Test
    public void timeTraveller() {
        givenDate1("2016-02-19 23:00:00");
        givenDate2("2016-01-19 23:00:00");
        thenResultIs("now");
    }

    @Test
    public void deltaExactlyWithinThreshold() {
        givenDate1("2016-01-19 23:00:00");
        givenDate2("2016-01-19 23:10:00");
        thenResultIs("now");
    }

    @Test
    public void deltaOneMinuteOverThreshold() {
        givenDate1("2016-01-19 23:00:00");
        givenDate2("2016-01-19 23:11:00");
        thenResultIs("11 minutes");
    }

    @Test
    public void deltaOneHour45Minutes() {
        givenDate1("2016-01-19 23:00:00");
        givenDate2("2016-01-20 00:45:00");
        thenResultIs("1 hour");
    }

    @Test
    public void deltaTwoHours5Minutes() {
        givenDate1("2016-01-19 23:00:00");
        givenDate2("2016-01-20 01:05:00");
        thenResultIs("2 hours");
    }

    @Test
    public void delta1Day() {
        givenDate1("2016-01-19 23:00:00");
        givenDate2("2016-01-20 23:00:00");
        thenResultIs("1 day");
    }

    @Test
    public void delta2Days() {
        givenDate1("2016-01-19 23:00:00");
        givenDate2("2016-01-21 23:10:01");
        thenResultIs("2 days");
    }

    @Test
    public void delta1Month() {
        givenDate1("2016-01-19 23:00:00");
        givenDate2("2016-02-19 23:00:00");
        thenResultIs("1 month");
    }

    @Test
    public void delta2Months() {
        givenDate1("2016-01-19 23:00:00");
        givenDate2("2016-03-19 23:00:00");
        thenResultIs("2 months");
    }

    @Test
    public void delta1Year() {
        givenDate1("2016-01-19 23:00:00");
        givenDate2("2017-01-19 23:00:00");
        thenResultIs("1 year");
    }

    @Test
    public void delta11Years() {
        givenDate1("2016-01-19 23:00:00");
        givenDate2("2027-01-19 23:00:00");
        thenResultIs("11 years");
    }

    private void givenDate1(String lastUpdate) {
        if (lastUpdate == null) {
            this.date1 = null;
        } else {
            this.date1 = a(date().with(lastUpdate));
        }
    }

    private void givenDate2(String currentDate) {
        if (currentDate == null) {
            this.date2 = null;
        } else {
            this.date2 = a(date().with(currentDate));
        }
    }

    private void thenResultIs(String expected) {
        assertEquals(expected, formatter.format(date1, date2));
    }
}