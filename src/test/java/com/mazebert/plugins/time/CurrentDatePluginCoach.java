package com.mazebert.plugins.time;

import java.util.Date;

public class CurrentDatePluginCoach extends CurrentDatePlugin {
    private Date currentDate = new Date();

    @Override
    public Date getCurrentDate() {
        return currentDate;
    }

    public void givenCurrentDate(Date date) {
        this.currentDate = date;
    }
}
