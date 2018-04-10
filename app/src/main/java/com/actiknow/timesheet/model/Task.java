package com.actiknow.timesheet.model;

/**
 * Created by sud on 8/4/18.
 */

public class Task {
    int project_id ;
    String no_of_hrs,date;

    public Task(int project_id, String no_of_hrs, String date) {
        this.project_id = project_id;
        this.no_of_hrs = no_of_hrs;
        this.date = date;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getNo_of_hrs() {
        return no_of_hrs;
    }

    public void setNo_of_hrs(String no_of_hrs) {
        this.no_of_hrs = no_of_hrs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
