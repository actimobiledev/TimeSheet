package com.actiknow.timesheet.model;

/**
 * Created by sud on 4/4/18.
 */

public class LeaveType {
    int type_id;
    String type_name, type_status, total, availed, remaining;
    
    public LeaveType (int type_id, String type_name, String type_status, String total, String availed, String remaining) {
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
    
    public String getType_status () {
        return type_status;
    }
    
    public void setType_status (String type_status) {
        this.type_status = type_status;
    }
    
    public String getTotal () {
        return total;
    }
    
    public void setTotal (String total) {
        this.total = total;
    }
    
    public String getAvailed () {
        return availed;
    }
    
    public void setAvailed (String availed) {
        this.availed = availed;
    }
    
    public String getRemaining () {
        return remaining;
    }
    
    public void setRemaining (String remaining) {
        this.remaining = remaining;
    }
}
