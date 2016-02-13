package com.mazebert.plugins.time.mocks;

import com.mazebert.plugins.time.CurrentDatePlugin;

import java.util.Date;

public class CurrentDatePluginMock extends CurrentDatePlugin {
    private Date currentDate = new Date();

    @Override
    public Date getCurrentDate() {
        return currentDate;
    }

    public void givenCurrentDate(Date date) {
        this.currentDate = date;
    }
}
