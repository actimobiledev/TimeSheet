package com.actiknow.timesheet.utils;

public class AppConfigURL {
    public static String version = "v1.3";
//    private static String BASE_URL = "https://project-surveyx-cammy92.c9users.io/api/" + version + "/";
    
    private static String BASE_URL = "http://34.215.95.251/pk/api/" + version + "/";
    
    
    public static String LOGIN = BASE_URL + "user/login";
    public static String START_SURVEY = BASE_URL + "survey/start";
    public static String INIT_APPLICATION = BASE_URL + "init/application";
    public static String CONCLUSION_SURVEY = BASE_URL + "survey/response/conclusion";
    public static String DAILY_SURVEY = BASE_URL + "survey/response/daily";
}