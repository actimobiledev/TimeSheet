package com.actiknow.timesheet.model;

/**
 * Created by sud on 8/4/18.
 */

public class Task {
    int project_id ;
    String no_of_hrs,date;
    String start_date,end_date,day_1,day_2,day_3,day_4,day_5,day_6,day_7;

    public Task(int project_id, String no_of_hrs, String date) {
        this.project_id = project_id;
        this.no_of_hrs = no_of_hrs;
        this.date = date;
    }

    public Task(int project_id, String start_date, String end_date, String day_1, String day_2, String day_3, String day_4, String day_5, String day_6, String day_7) {
        this.project_id = project_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.day_1 = day_1;
        this.day_2 = day_2;
        this.day_3 = day_3;
        this.day_4 = day_4;
        this.day_5 = day_5;
        this.day_6 = day_6;
        this.day_7 = day_7;
    }


    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDay_1() {
        return day_1;
    }

    public void setDay_1(String day_1) {
        this.day_1 = day_1;
    }

    public String getDay_2() {
        return day_2;
    }

    public void setDay_2(String day_2) {
        this.day_2 = day_2;
    }

    public String getDay_3() {
        return day_3;
    }

    public void setDay_3(String day_3) {
        this.day_3 = day_3;
    }

    public String getDay_4() {
        return day_4;
    }

    public void setDay_4(String day_4) {
        this.day_4 = day_4;
    }

    public String getDay_5() {
        return day_5;
    }

    public void setDay_5(String day_5) {
        this.day_5 = day_5;
    }

    public String getDay_6() {
        return day_6;
    }

    public void setDay_6(String day_6) {
        this.day_6 = day_6;
    }

    public String getDay_7() {
        return day_7;
    }

    public void setDay_7(String day_7) {
        this.day_7 = day_7;
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
