package com.mazebert.entities;

import com.mazebert.plugins.time.CurrentDatePlugin;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class BlackMarket {
    private final CurrentDatePlugin currentDatePlugin;

    public BlackMarket(CurrentDatePlugin currentDatePlugin) {
        this.currentDatePlugin = currentDatePlugin;
    }

    public boolean isAvailable(TimeZone timeZone) {
        Date now = currentDatePlugin.getCurrentDate();

        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(now);
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
}
