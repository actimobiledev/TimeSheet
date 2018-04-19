package com.actiknow.timesheet.model;

/**
 * Created by sud on 4/4/18.
 */

public class Project {
    int id;
    String project_title, client_name, created_by, project_hours;
    
    public Project (int id, String project_title, String client_name, String created_by, String project_hours) {
        this.id = id;
        this.project_title = project_title;
        this.client_name = client_name;
        this.created_by = created_by;
        this.project_hours = project_hours;
    }
    
    
    public String getProject_hours () {
        return project_hours;
    }
    
    public void setProject_hours (String project_hours) {
        this.project_hours = project_hours;
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
    
    public String getClient_name () {
        return client_name;
    }
    
    public void setClient_name (String client_name) {
        this.client_name = client_name;
    }
    
    public String getCreated_by () {
        return created_by;
    }
    
    public void setCreated_by (String created_by) {
        this.created_by = created_by;
    }
}