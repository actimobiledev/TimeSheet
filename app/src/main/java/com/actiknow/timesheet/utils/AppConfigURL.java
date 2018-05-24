package com.actiknow.timesheet.utils;

public class AppConfigURL {
    public static String version = "v1.4";
    
    //    private static String BASE_URL = "http://34.215.95.251/timesheet/api/" + version + "/";
    private static String BASE_URL = " https://project-timesheet-cammy92.c9users.io/api/" + version + "/";

    public static String LOGIN = BASE_URL + "login";
    public static String HOME = BASE_URL + "home";
    public static String PROJECTS = BASE_URL + "projects";
    public static String ADD_CLIENT = BASE_URL + "client";
    public static String ADD_PROJECT = BASE_URL + "project";
    public static String ADD_TASK = BASE_URL + "add/task";
    public static String PREVIOUS_WEEK = BASE_URL + "projects/previous-week";
    public static String ADD_PROJECT_OWNER = BASE_URL + "project/owner";
    public static String CHANGE_PASSWORD = BASE_URL + "employee/change-password";
    public static String FORGOT_PASSWORD = BASE_URL + "employee/forgot-password";
    public static String URL_INIT = BASE_URL + "init/application";
    public static String URL_FEEDBACK = BASE_URL + "feedback";
    
    public static String URL_CLIENT_PROJECT = BASE_URL + "projects/client";
    
    // public static String PREVIOUS_WEEK =" https://project-timesheet-cammy92.c9users.io/api/v1.0/previous_week_task";
    
    public static String MY_EMPLOYEES = BASE_URL + "manager/employees";
    public static String DELETE_MY_EMPLOYEES = BASE_URL + "manager/employee";
    public static String ADD_MY_EMPLOYEES = BASE_URL + "manager/employee";
    
    public static String DELETE_PROJECT_OWNER = BASE_URL + "";
    
    public static String URL_LEAVE_PORTAL = BASE_URL + "leave-portal";
    public static String URL_APPLY_LEAVE = BASE_URL + "leave/apply";
    public static String URL_UPDATE_LEAVE = BASE_URL + "leave/update";
    public static String URL_CANCEL_LEAVE = BASE_URL + "leave/cancel";
}

