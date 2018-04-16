package com.actiknow.timesheet.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class AppDetailsPref {
    public static String EMPLOYEE_NAME = "employee_name";
    public static String EMPLOYEE_MOBILE = "employee_mobile";
    public static String EMPLOYEE_WORK_EMAIL = "employee_work_email";
    public static String EMPLOYEE_IMAGE = "employee_image";
    public static String EMPLOYEE_LOGIN_KEY = "employee_login_key";
    public static String EMPLOYEE_TYPE = "employee_type";
    
    public static String CLIENTS = "clients";
    public static String EMPLOYEES = "employees";
    public static String ROLES = "roles";







    
    private static AppDetailsPref appDetailsPref;
    private String APP_DETAILS = "APP_DETAILS";
    
    public static AppDetailsPref getInstance () {
        if (appDetailsPref == null)
            appDetailsPref = new AppDetailsPref ();
        return appDetailsPref;
    }

    private SharedPreferences getPref (Context context) {
        return context.getSharedPreferences (APP_DETAILS, Context.MODE_PRIVATE);
    }

    public String getStringPref (Context context, String key) {
        return getPref (context).getString (key, "");
    }

    public int getIntPref (Context context, String key) {
        return getPref (context).getInt (key, 0);
    }

    public boolean getBooleanPref (Context context, String key) {
        return getPref (context).getBoolean (key, false);
    }

    public void putBooleanPref (Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getPref (context).edit ();
        editor.putBoolean (key, value);
        editor.apply ();
    }

    public void putStringPref (Context context, String key, String value) {
        SharedPreferences.Editor editor = getPref (context).edit ();
        editor.putString (key, value);
        editor.apply ();
    }

    public void putIntPref (Context context, String key, int value) {
        SharedPreferences.Editor editor = getPref (context).edit ();
        editor.putInt (key, value);
        editor.apply ();
    }
}
