package com.actiknow.timesheet.model;

public class MyEmployee {
    int employee_id;
    String employee_name, total_json, projects_json;
    
    public MyEmployee (int employee_id, String employee_name, String total_json, String projects_json) {
        this.employee_id = employee_id;
        this.employee_name = employee_name;
        this.total_json = total_json;
        this.projects_json = projects_json;
    }
    
    public int getEmployee_id () {
        return employee_id;
    }
    
    public void setEmployee_id (int employee_id) {
        this.employee_id = employee_id;
    }
    
    public String getEmployee_name () {
        return employee_name;
    }
    
    public void setEmployee_name (String employee_name) {
        this.employee_name = employee_name;
    }
    
    public String getTotal_json () {
        return total_json;
    }
    
    public void setTotal_json (String total_json) {
        this.total_json = total_json;
    }
    
    public String getProjects_json () {
        return projects_json;
    }
    
    public void setProjects_json (String projects_json) {
        this.projects_json = projects_json;
    }
}

