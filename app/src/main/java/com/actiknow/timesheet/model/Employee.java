package com.actiknow.timesheet.model;

/**
 * Created by l on 17/04/2018.
 */

public class Employee {
    int employee_id;
    String employee_name, employee_work_email;
    
    public Employee (int employee_id, String employee_name, String employee_work_email) {
        this.employee_id = employee_id;
        this.employee_name = employee_name;
        this.employee_work_email = employee_work_email;
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
    
    public String getEmployee_work_email () {
        return employee_work_email;
    }
    
    public void setEmployee_work_email (String employee_work_email) {
        this.employee_work_email = employee_work_email;
    }
}

