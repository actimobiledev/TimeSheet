package com.actiknow.timesheet.model;

/**
 * Created by sud on 4/4/18.
 */

public class Client {
    int id;
    String name;
    
    public Client (int id, String name) {
        this.id = id;
        this.name = name;
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
}
