package com.actiknow.timesheet.model;

/**
 * Created by sud on 4/4/18.
 */

public class MyEmployeeProject {
    int id;
    String project_title, hours_json;
    
    public MyEmployeeProject (int id, String project_title, String hours_json) {
        this.id = id;
        this.project_title = project_title;
        this.hours_json = hours_json;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getProject_title () {
        return project_title;
    }
    
    public void setProject_title (String project_title) {
        this.project_title = project_title;
    }
    
    public String getHours_json () {
        return hours_json;
    }
    
    public void setHours_json (String hours_json) {
        this.hours_json = hours_json;
    }
}