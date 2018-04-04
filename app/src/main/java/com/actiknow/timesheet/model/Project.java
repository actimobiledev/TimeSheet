package com.actiknow.timesheet.model;

/**
 * Created by sud on 4/4/18.
 */

public class Project {
    int id;
    String name, description, hours;

    public Project(int id, String name, String description, String hours) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.hours = hours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
