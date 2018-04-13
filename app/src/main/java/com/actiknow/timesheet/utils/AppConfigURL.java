package com.actiknow.timesheet.utils;

public class AppConfigURL {
    public static String version = "v1.1";
//    private static String BASE_URL = "https://project-surveyx-cammy92.c9users.io/api/" + version + "/";

    // private static String BASE_URL = "http://34.215.95.251/timesheet/api/" + version + "/";
    private static String BASE_URL = " https://project-timesheet-cammy92.c9users.io/api/" + version + "/";

    public static String LOGIN = BASE_URL + "login";
    public static String CLIENTS = BASE_URL + "client";
    public static String PROJECTS = BASE_URL + "project";
    public static String ADD_CLIENT = BASE_URL + "add/client";
    public static String ADD_PROJECT = BASE_URL + "add/project";
    public static String START_SURVEY = BASE_URL + "survey/start";
    public static String INIT_APPLICATION = BASE_URL + "init/application";
    public static String CONCLUSION_SURVEY = BASE_URL + "survey/response/conclusion";
    public static String DAILY_SURVEY = BASE_URL + "survey/response/daily";
    public static String ADD_TASK = BASE_URL + "add/task3";
    public static String ADD_TASK4 = BASE_URL + "add/task4";
    public static String PREVIOUS_WEEK = BASE_URL + "previous_week_task";
    public static String ADD_PROJECT_OWNER = BASE_URL + "add/project_owner";
    public static String CHANGE_PASSWORD = BASE_URL + "employee/change-password";
    public static String FORGOT_PASSWORD = BASE_URL + "employee/forgot-password";


    // public static String PREVIOUS_WEEK =" https://project-timesheet-cammy92.c9users.io/api/v1.0/previous_week_task";


}

