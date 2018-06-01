package com.actiknow.timesheet.model;

public class Leave {
    int request_id, type_id, status, employee_id;
    double leaves_availed;
    String type_name, leave_from, leave_till, description, applied_at, updated_at, updated_by, remark, employee_name;
    
    public Leave (int request_id, int type_id, int status, double leaves_availed, String type_name, String leave_from, String leave_till, String description, String applied_at, String updated_at, String updated_by, String remark) {
        this.request_id = request_id;
        this.type_id = type_id;
        this.status = status;
        this.leaves_availed = leaves_availed;
        this.employee_id = 0;
        this.type_name = type_name;
        this.leave_from = leave_from;
        this.leave_till = leave_till;
        this.description = description;
        this.applied_at = applied_at;
        this.updated_at = updated_at;
        this.updated_by = updated_by;
        this.remark = remark;
        this.employee_name = "";
    }
    
    public Leave (int request_id, int type_id, int status, int employee_id, double leaves_availed, String type_name, String leave_from, String leave_till, String description, String applied_at, String updated_at, String updated_by, String remark, String employee_name) {
        this.request_id = request_id;
        this.type_id = type_id;
        this.status = status;
        this.employee_id = employee_id;
        this.leaves_availed = leaves_availed;
        this.type_name = type_name;
        this.leave_from = leave_from;
        this.leave_till = leave_till;
        this.description = description;
        this.applied_at = applied_at;
        this.updated_at = updated_at;
        this.updated_by = updated_by;
        this.remark = remark;
        this.employee_name = employee_name;
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
    
    public int getRequest_id () {
        return request_id;
    }
    
    public void setRequest_id (int request_id) {
        this.request_id = request_id;
    }
    
    public int getType_id () {
        return type_id;
    }
    
    public void setType_id (int type_id) {
        this.type_id = type_id;
    }
    
    public int getStatus () {
        return status;
    }
    
    public void setStatus (int status) {
        this.status = status;
    }
    
    public double getLeaves_availed () {
        return leaves_availed;
    }
    
    public void setLeaves_availed (double leaves_availed) {
        this.leaves_availed = leaves_availed;
    }
    
    public String getType_name () {
        return type_name;
    }
    
    public void setType_name (String type_name) {
        this.type_name = type_name;
    }
    
    public String getLeave_from () {
        return leave_from;
    }
    
    public void setLeave_from (String leave_from) {
        this.leave_from = leave_from;
    }
    
    public String getLeave_till () {
        return leave_till;
    }
    
    public void setLeave_till (String leave_till) {
        this.leave_till = leave_till;
    }
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
    
    public String getApplied_at () {
        return applied_at;
    }
    
    public void setApplied_at (String applied_at) {
        this.applied_at = applied_at;
    }
    
    public String getUpdated_at () {
        return updated_at;
    }
    
    public void setUpdated_at (String updated_at) {
        this.updated_at = updated_at;
    }
    
    public String getUpdated_by () {
        return updated_by;
    }
    
    public void setUpdated_by (String updated_by) {
        this.updated_by = updated_by;
    }
    
    public String getRemark () {
        return remark;
    }
    
    public void setRemark (String remark) {
        this.remark = remark;
    }
}
