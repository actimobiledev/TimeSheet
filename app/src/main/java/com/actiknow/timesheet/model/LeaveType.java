package com.actiknow.timesheet.model;

/**
 * Created by sud on 4/4/18.
 */

public class LeaveType {
    int type_id, type_status;
    String type_name;
    double total, availed, remaining;
    
    public LeaveType (int type_id, String type_name, int type_status, double total, double availed, double remaining) {
        this.type_id = type_id;
        this.type_name = type_name;
        this.type_status = type_status;
        this.total = total;
        this.availed = availed;
        this.remaining = remaining;
    }
    
    public int getType_id () {
        return type_id;
    }
    
    public void setType_id (int type_id) {
        this.type_id = type_id;
    }
    
    public String getType_name () {
        return type_name;
    }
    
    public void setType_name (String type_name) {
        this.type_name = type_name;
    }
    
    public int getType_status () {
        return type_status;
    }
    
    public void setType_status (int type_status) {
        this.type_status = type_status;
    }
    
    public double getTotal () {
        return total;
    }
    
    public void setTotal (double total) {
        this.total = total;
    }
    
    public double getAvailed () {
        return availed;
    }
    
    public void setAvailed (double availed) {
        this.availed = availed;
    }
    
    public double getRemaining () {
        return remaining;
    }
    
    public void setRemaining (double remaining) {
        this.remaining = remaining;
    }
}
