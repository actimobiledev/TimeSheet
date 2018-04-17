package com.actiknow.timesheet.model;

/**
 * Created by l on 17/04/2018.
 */

public class Employee2 {
    int employee_id;
    String employee_name, role;
    
    public Employee2 (int employee_id, String employee_name, String role) {
        this.employee_id = employee_id;
        this.employee_name = employee_name;
        this.role = role;
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
    
    public String getRole () {
        return role;
    }
    
    public void setRole (String role) {
        this.role = role;
    }
}

