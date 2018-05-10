package com.actiknow.timesheet.model;

/**
 * Created by sud on 4/4/18.
 */

public class Holiday {
    int id;
    String day, date, holiday_name;
    
    public Holiday (int id, String day, String date, String holiday_name) {
        this.id = id;
        this.day = day;
        this.date = date;
        this.holiday_name = holiday_name;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getDay () {
        return day;
    }
    
    public void setDay (String day) {
        this.day = day;
    }
    
    public String getDate () {
        return date;
    }
    
    public void setDate (String date) {
        this.date = date;
    }
    
    public String getHoliday_name () {
        return holiday_name;
    }
    
    public void setHoliday_name (String holiday_name) {
        this.holiday_name = holiday_name;
    }
}
