package com.actiknow.timesheet.model;

/**
 * Created by sud on 4/4/18.
 */

public class Project {
    int id,client_id;
    String project_title,project_budget,hour_cost,alloted_hour,description, planned_start,planned_complete,started_at,complete_at,status;

    public Project(int id, int client_id, String project_title, String project_budget, String hour_cost, String alloted_hour, String description, String planned_start, String planned_complete, String started_at, String complete_at, String status) {
        this.id = id;
        this.client_id = client_id;
        this.project_title = project_title;
        this.project_budget = project_budget;
        this.hour_cost = hour_cost;
        this.alloted_hour = alloted_hour;
        this.description = description;
        this.planned_start = planned_start;
        this.planned_complete = planned_complete;
        this.started_at = started_at;
        this.complete_at = complete_at;
        this.status = status;
    }

    public Project(int id,String project_title,String description, String started_at) {
        this.id = id;
        this.description = description;
        this.project_title = project_title;
        this.started_at = started_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getProject_title() {
        return project_title;
    }

    public void setProject_title(String project_title) {
        this.project_title = project_title;
    }

    public String getProject_budget() {
        return project_budget;
    }

    public void setProject_budget(String project_budget) {
        this.project_budget = project_budget;
    }

    public String getHour_cost() {
        return hour_cost;
    }

    public void setHour_cost(String hour_cost) {
        this.hour_cost = hour_cost;
    }

    public String getAlloted_hour() {
        return alloted_hour;
    }

    public void setAlloted_hour(String alloted_hour) {
        this.alloted_hour = alloted_hour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlanned_start() {
        return planned_start;
    }

    public void setPlanned_start(String planned_start) {
        this.planned_start = planned_start;
    }

    public String getPlanned_complete() {
        return planned_complete;
    }

    public void setPlanned_complete(String planned_complete) {
        this.planned_complete = planned_complete;
    }

    public String getStarted_at() {
        return started_at;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public String getComplete_at() {
        return complete_at;
    }

    public void setComplete_at(String complete_at) {
        this.complete_at = complete_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
