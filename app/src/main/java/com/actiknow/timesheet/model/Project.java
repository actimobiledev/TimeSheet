package com.actiknow.timesheet.model;

/**
 * Created by sud on 4/4/18.
 */

public class Project {
    int id;
    String project_title, client_name;
    
    public Project (int id, String project_title, String client_name) {
        this.id = id;
        this.project_title = project_title;
        this.client_name = client_name;
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
}
