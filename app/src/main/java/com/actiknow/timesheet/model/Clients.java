package com.actiknow.timesheet.model;

/**
 * Created by sud on 4/4/18.
 */

public class Clients {
    int id,emp_id;
    String name,email,contact,skype,source,location, company;


    public Clients(int id, int emp_id, String name, String email, String contact, String skype, String source, String location, String company) {
        this.id = id;
        this.emp_id = emp_id;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.skype = skype;
        this.source = source;
        this.location = location;
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
