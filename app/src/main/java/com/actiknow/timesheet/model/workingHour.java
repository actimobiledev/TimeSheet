package com.actiknow.timesheet.model;

/**
 * Created by l on 09/04/2018.
 */

public class workingHour {
    String date,hour;

    public workingHour(String date, String hour) {
        this.date = date;
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}

