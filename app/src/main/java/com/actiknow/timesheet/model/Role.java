package com.actiknow.timesheet.model;

/**
 * Created by l on 17/04/2018.
 */

public class Role {
    int role_id;
    String role_title;
    
    public Role (int role_id, String role_title) {
        this.role_id = role_id;
        this.role_title = role_title;
    }
    
    public int getRole_id () {
        return role_id;
    }
    
    public void setRole_id (int role_id) {
        this.role_id = role_id;
    }
    
    public String getRole_title () {
        return role_title;
    }
    
    public void setRole_title (String role_title) {
        this.role_title = role_title;
    }
}

