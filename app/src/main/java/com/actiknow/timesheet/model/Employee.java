package com.actiknow.timesheet.model;

/**
 * Created by l on 17/04/2018.
 */

public class Employee {
    int id;
    String name, work_email;
    
    public Employee (int id, String name, String work_email) {
        this.id = id;
        this.name = name;
        this.work_email = work_email;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getName () {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    public String getWork_email () {
        return work_email;
    }
    
    public void setWork_email (String work_email) {
        this.work_email = work_email;
    }
}

