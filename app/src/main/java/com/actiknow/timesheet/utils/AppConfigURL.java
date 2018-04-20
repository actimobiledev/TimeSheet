package com.actiknow.timesheet.utils;

public class AppConfigURL {
    public static String version = "v1.2";
    
    private static String BASE_URL = "http://34.215.95.251/timesheet/api/" + version + "/";
    //private static String BASE_URL = " https://project-timesheet-cammy92.c9users.io/api/" + version + "/";

    public static String LOGIN = BASE_URL + "login";
    public static String CLIENTS = BASE_URL + "client";
    public static String HOME = BASE_URL + "home";
    public static String PROJECTS = BASE_URL + "projects";
    public static String ADD_CLIENT = BASE_URL + "client";
    public static String ADD_PROJECT = BASE_URL + "project";
    public static String START_SURVEY = BASE_URL + "survey/start";
    public static String INIT_APPLICATION = BASE_URL + "init/application";
    public static String CONCLUSION_SURVEY = BASE_URL + "survey/response/conclusion";
    public static String DAILY_SURVEY = BASE_URL + "survey/response/daily";
    public static String ADD_TASK = BASE_URL + "add/task";
    public static String ADD_TASK4 = BASE_URL + "add/task4";
    public static String PREVIOUS_WEEK = BASE_URL + "projects/previous-week";
    public static String ADD_PROJECT_OWNER = BASE_URL + "project/owner";
    public static String CHANGE_PASSWORD = BASE_URL + "employee/change-password";
    public static String FORGOT_PASSWORD = BASE_URL + "employee/forgot-password";
    public static String URL_INIT = BASE_URL + "init/application";
    public static String URL_FEEDBACK = BASE_URL + "feedback";
    
    public static String URL_CLIENT_PROJECT = BASE_URL + "projects/client";
    
    // public static String PREVIOUS_WEEK =" https://project-timesheet-cammy92.c9users.io/api/v1.0/previous_week_task";


}

