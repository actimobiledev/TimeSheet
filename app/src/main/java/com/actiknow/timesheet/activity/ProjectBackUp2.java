package com.actiknow.timesheet.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.Task;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppConfigURL;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Constants;
import com.actiknow.timesheet.utils.NetworkConnection;
import com.actiknow.timesheet.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by l on 27/07/2017.
 */

public class ProjectBackUp2 extends AppCompatActivity {
    Bundle savedInstanceState;
    ProgressDialog progressDialog;
    CoordinatorLayout clMain;
    String projects = "";
    int position = 0;
    int project_id = 0;
    int array_length = 0;
    int i = 0;
    ArrayList<Task> tasklist = new ArrayList<> ();
    String day1 = "0";
    String day2 = "0";
    String day3 = "0";
    String day4 = "0";
    String day5 = "0";
    String day6 = "0";
    String day7 = "0";
    AppDetailsPref appDetailsPref = AppDetailsPref.getInstance ();
    private RelativeLayout rlBack;
    private ImageView ivBack;
    private TextView tvTitle;
    private RelativeLayout rlList;
    private LinearLayout llProjectName;
    private ImageView ivPreProject;
    private ImageView ivNextProject;
    private LinearLayout llDay1;
    private TextView tvDate1;
    private EditText etMondayhour;
    private LinearLayout llDay2;
    private TextView tvDate2;
    private EditText etTueshour;
    private LinearLayout llDay3;
    private TextView tvDate3;
    private EditText etWednesdayHour;
    private LinearLayout llDay4;
    private TextView tvDate4;
    private EditText etThursdayhour;
    private LinearLayout llDay5;
    private TextView tvDate5;
    private EditText etFridayhour;
    private LinearLayout llDay6;
    private TextView tvDate6;
    private EditText etSaturdayhour;
    private LinearLayout llDay7;
    private TextView tvDate7;
    private EditText etSundayhour;
    private TextView tvSubmit;
    private TextView tvProjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail2);
        this.savedInstanceState = savedInstanceState;
        initView();
        initData();
        setData();
        initListener();

        //    recommendedJobList ();
    }

    private void setData() {

        try {
            projects = getIntent ().getExtras ().getString ("projects_json");
            position = getIntent().getExtras().getInt("position", 0);

            JSONArray jsonArray = new JSONArray(projects);
            array_length = jsonArray.length();
            Log.e("lenght", "" + array_length);
            if (jsonArray.length() > 0) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    if (j == position) {
                        tvProjectName.setText(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                        project_id = jsonObject.getInt(AppConfigTags.PROJECT_ID);

                        JSONArray jsonArrayHour = jsonObject.getJSONArray(AppConfigTags.HOURS);
                        Log.e("jsonArrayHour", jsonArrayHour.toString());
                        for (int i = 0; i < jsonArrayHour.length(); i++) {
                            JSONObject jsonObject2 = jsonArrayHour.getJSONObject(i);


                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String inputDateStr = jsonObject2.getString(AppConfigTags.DATE);

                            Date date = null;
                            date = inputFormat.parse(inputDateStr);
                            String Date = outputFormat.format(date);
                            Log.e("date1234", Date);

                            if (Date.equalsIgnoreCase(tvDate1.getText().toString())) {
                                etMondayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                            }
                            if (Date.equalsIgnoreCase(tvDate2.getText().toString())) {
                                etTueshour.setText(jsonObject2.getString(AppConfigTags.hour));
                            }
                            if (Date.equalsIgnoreCase(tvDate3.getText().toString())) {
                                etWednesdayHour.setText(jsonObject2.getString(AppConfigTags.hour));
                            }
                            if (Date.equalsIgnoreCase(tvDate4.getText().toString())) {
                                etThursdayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                            }
                            if (Date.equalsIgnoreCase(tvDate5.getText().toString())) {
                                etFridayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                            }
                            if (Date.equalsIgnoreCase(tvDate6.getText().toString())) {
                                etSaturdayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                            }
                            if (Date.equalsIgnoreCase(tvDate7.getText().toString())) {
                                etSundayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                            }
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //   getProjectList();
        // put your code here...

    }


    private void initView() {
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        rlList = (RelativeLayout) findViewById(R.id.rlList);
        llProjectName = (LinearLayout) findViewById(R.id.llProjectName);
        ivPreProject = (ImageView) findViewById(R.id.ivPreProject);
        ivNextProject = (ImageView) findViewById(R.id.ivNextProject);
        llDay1 = (LinearLayout) findViewById(R.id.llDay1);
        tvDate1 = (TextView) findViewById(R.id.tvDate1);
        etMondayhour = (EditText) findViewById(R.id.etMondayhour);
        llDay2 = (LinearLayout) findViewById(R.id.llDay2);
        tvDate2 = (TextView) findViewById(R.id.tvDate2);
        etTueshour = (EditText) findViewById(R.id.etTueshour);
        llDay3 = (LinearLayout) findViewById(R.id.llDay3);
        tvDate3 = (TextView) findViewById(R.id.tvDate3);
        etWednesdayHour = (EditText) findViewById(R.id.etWednesdayHour);
        llDay4 = (LinearLayout) findViewById(R.id.llDay4);
        tvDate4 = (TextView) findViewById(R.id.tvDate4);
        etThursdayhour = (EditText) findViewById(R.id.etThursdayhour);
        llDay5 = (LinearLayout) findViewById(R.id.llDay5);
        tvDate5 = (TextView) findViewById(R.id.tvDate5);
        etFridayhour = (EditText) findViewById(R.id.etFridayhour);
        llDay6 = (LinearLayout) findViewById(R.id.llDay6);
        tvDate6 = (TextView) findViewById(R.id.tvDate6);
        etSaturdayhour = (EditText) findViewById(R.id.etSaturdayhour);
        llDay7 = (LinearLayout) findViewById(R.id.llDay7);
        tvDate7 = (TextView) findViewById(R.id.tvDate7);
        etSundayhour = (EditText) findViewById(R.id.etSundayhour);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvProjectName = (TextView) findViewById(R.id.tvProjectName);
        ivPreProject = (ImageView) findViewById(R.id.ivPreProject);
        ivNextProject = (ImageView) findViewById(R.id.ivNextProject);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);

    }


    private void initListener() {
        ivNextProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day1 = etMondayhour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etMondayhour.getText().toString();
                day2 = etTueshour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etTueshour.getText().toString();
                day3 = etWednesdayHour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etWednesdayHour.getText().toString();
                day4 = etThursdayhour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etThursdayhour.getText().toString();
                day5 = etFridayhour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etFridayhour.getText().toString();
                day6 = etSaturdayhour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etSaturdayhour.getText().toString();
                day7 = etSundayhour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etSundayhour.getText().toString();


                if (array_length == position + 1) {
                    try {
                        JSONArray jsonArray = new JSONArray(projects);
                        JSONObject jsonObject = jsonArray.getJSONObject(array_length - 1);
                        tvProjectName.setText(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                        project_id = jsonArray.getJSONObject(array_length - 1).getInt(AppConfigTags.PROJECT_ID);

                        JSONArray jsonArrayHour = jsonObject.getJSONArray(AppConfigTags.HOURS);

                        if (jsonArrayHour.length()>0) {
                            Log.e("jsonArrayHour", jsonArrayHour.toString());
                            for (int i = 0; i < jsonArrayHour.length(); i++) {
                                JSONObject jsonObject2 = jsonArrayHour.getJSONObject(i);

                                if (Utils.dateFormat2(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate1.getText().toString())) {
                                    etMondayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                                if (Utils.dateFormat2(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate2.getText().toString())) {
                                    etTueshour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                                if (Utils.dateFormat2(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate3.getText().toString())) {
                                    etWednesdayHour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                                if (Utils.dateFormat2(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate4.getText().toString())) {
                                    etThursdayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                                if (Utils.dateFormat2(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate5.getText().toString())) {
                                    etFridayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                                if (Utils.dateFormat2(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate6.getText().toString())) {
                                    etSaturdayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                                if (Utils.dateFormat2(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate7.getText().toString())) {
                                    etSundayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                            }
                        }

                        tasklist.add(new Task(project_id,
                                Utils.dateFormat2(tvDate1.getText().toString()),
                                Utils.dateFormat2(tvDate7.getText().toString()),
                                day1,
                                day2,
                                day3,
                                day4,
                                day5,
                                day6,
                                day7));

                        for (int i = 0; i < tasklist.size(); i++) {
                            Log.e("timesheet", String.valueOf(tasklist.get(i).getProject_id() + "-" + tasklist.get(i).getStart_date() + "-" + tasklist.get(i).getEnd_date() + " day1- " + day1 + " day2- " + day2 + " day3- " + day3 + " day4- " + day4 + " day5- " + day5 + " day6- " + day6 + " day7- " + day7));

                        }
                        ivNextProject.setEnabled(false);


       /*  for (Task t : tasklist) {
                            if (t.getProject_id() == project_id && t.getDate().equalsIgnoreCase(tvDate1.getText().toString())) {
                                etMondayhour.setText("" + t.getNo_of_hrs());
                            }
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ivNextProject.setEnabled(true);
                }
/*
                etMondayhour.setText("");
                etTueshour.setText("");
                etWednesdayHour.setText("");
                etThursdayhour.setText("");
                etFridayhour.setText("");
                etSaturdayhour.setText("");
                etSundayhour.setText("");*/


                if (array_length - 1 > position) {
                    position = position + 1;
                    try {
                        JSONArray jsonArray = new JSONArray(projects);
                        JSONObject jsonObject = jsonArray.getJSONObject(position);
                        tvProjectName.setText(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                        project_id = jsonArray.getJSONObject(position - 1).getInt(AppConfigTags.PROJECT_ID);
                        JSONArray jsonArrayHour = jsonObject.getJSONArray(AppConfigTags.HOURS);
                        if (jsonArrayHour.length()>0){
                            Log.e("jsonArrayHour", jsonArrayHour.toString());
                            for (int i = 0; i < jsonArrayHour.length(); i++) {
                                JSONObject jsonObject2 = jsonArrayHour.getJSONObject(i);

                                if (Utils.dateFormat(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate1.getText().toString())) {
                                    etMondayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                                if (Utils.dateFormat(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate2.getText().toString())) {
                                    etTueshour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                                if (Utils.dateFormat(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate3.getText().toString())) {
                                    etWednesdayHour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                                if (Utils.dateFormat(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate4.getText().toString())) {
                                    etThursdayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                                if (Utils.dateFormat(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate5.getText().toString())) {
                                    etFridayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                                if (Utils.dateFormat(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate6.getText().toString())) {
                                    etSaturdayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                                if (Utils.dateFormat(jsonObject2.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate7.getText().toString())) {
                                    etSundayhour.setText(jsonObject2.getString(AppConfigTags.hour));
                                }
                            }
                        }else{

                            etMondayhour.setText("");
                            etTueshour.setText("");
                            etWednesdayHour.setText("");
                            etThursdayhour.setText("");
                            etFridayhour.setText("");
                            etSaturdayhour.setText("");
                            etSundayhour.setText("");
                        }


                        tasklist.add(new Task(project_id, Utils.dateFormat2(tvDate1.getText().toString()), Utils.dateFormat2(tvDate7.getText().toString()),
                                day1, day2, day3, day4, day5, day6, day7));


                        for (int i = 0; i < tasklist.size(); i++) {
                            Log.e("timesheet", String.valueOf(tasklist.get(i).getProject_id() + "-" + tasklist.get(i).getStart_date() + "-" + tasklist.get(i).getEnd_date() + " day1- " + day1 + " day2- " + day2 + " day3- " + day3 + " day4- " + day4 + " day5- " + day5 + " day6- " + day6 + " day7- " + day7));

                        }

                      /*  for (Task t : tasklist) {
                            if (t.getProject_id() == project_id && t.getDate().equalsIgnoreCase(tvDate1.getText().toString())) {
                                etMondayhour.setText("" + t.getNo_of_hrs());
                            }
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }


            }
        });
        ivPreProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position > 0) {
                    position = position - 1;
                    try {
                        JSONArray jsonArray = new JSONArray(projects);
                        JSONObject jsonObject = jsonArray.getJSONObject(position);
                        tvProjectName.setText(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                        project_id = jsonObject.getInt(AppConfigTags.PROJECT_ID);
                        /*for(Task t : tasklist){
                            if(t.getProject_id() == project_id && t.getDate().equalsIgnoreCase(tvDate1.getText().toString())){
                                etMondayhour.setText(""+t.getDate());
                            }
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


    }

    private void initData() {
        progressDialog = new ProgressDialog(ProjectBackUp2.this);
        Calendar c = Calendar.getInstance(); // Set the calendar to Sunday of the current week
        c.set(Calendar.DAY_OF_WEEK, c.MONDAY); // Print dates of the current week starting on Sunday
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 1; i < 8; i++) {
            switch (i) {
                case 1:
                    tvDate1.setText(df.format(c.getTime()));
                    break;
                case 2:
                    tvDate2.setText(df.format(c.getTime()));
                    break;
                case 3:
                    tvDate3.setText(df.format(c.getTime()));
                    break;
                case 4:
                    tvDate4.setText(df.format(c.getTime()));
                    break;
                case 5:
                    tvDate5.setText(df.format(c.getTime()));
                    break;
                case 6:
                    tvDate6.setText(df.format(c.getTime()));
                    break;
                case 7:
                    tvDate7.setText(df.format(c.getTime()));
                    break;
            }

            c.add(Calendar.DATE, 1);


        }


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tasklist.size()==0){
                    day1 = etMondayhour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etMondayhour.getText().toString();
                    day2 = etTueshour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etTueshour.getText().toString();
                    day3 = etWednesdayHour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etWednesdayHour.getText().toString();
                    day4 = etThursdayhour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etThursdayhour.getText().toString();
                    day5 = etFridayhour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etFridayhour.getText().toString();
                    day6 = etSaturdayhour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etSaturdayhour.getText().toString();
                    day7 = etSundayhour.getText().toString().trim().equalsIgnoreCase("") ? "0" : etSundayhour.getText().toString();

                    tasklist.add(new Task(project_id, Utils.dateFormat2(tvDate1.getText().toString()), Utils.dateFormat2(tvDate7.getText().toString()),
                            day1, day2, day3, day4, day5, day6, day7));
                }


                if (array_length == position + 1) {
                            try {
                                JSONArray jsonArray = new JSONArray(projects);
                                JSONObject jsonObject = jsonArray.getJSONObject(array_length - 1);
                                tvProjectName.setText(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                                project_id = jsonArray.getJSONObject(array_length - 1).getInt(AppConfigTags.PROJECT_ID);

                        for(int i=0; i<tasklist.size(); i++) {
                            if (!(tasklist.get(i).getProject_id() == project_id)) {

                                tasklist.add(new Task(project_id, Utils.dateFormat2(tvDate1.getText().toString()), Utils.dateFormat2(tvDate7.getText().toString()),
                                        day1, day2, day3, day4, day5, day6, day7));
                            }
                        }

                        for (int i = 0; i < tasklist.size(); i++) {
                            Log.e("timesheet", String.valueOf(tasklist.get(i).getProject_id() + "-" + tasklist.get(i).getStart_date() + "-" + tasklist.get(i).getEnd_date() + " day1- " + day1 + " day2- " + day2 + " day3- " + day3 + " day4- " + day4 + " day5- " + day5 + " day6- " + day6 + " day7- " + day7));

                        }

                      /*  for (Task t : tasklist) {
                            if (t.getProject_id() == project_id && t.getDate().equalsIgnoreCase(tvDate1.getText().toString())) {
                                etMondayhour.setText("" + t.getNo_of_hrs());
                            }
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }


              /*  for(int i=0; i<tasklist.size(); i++){
                    if(tasklist.get(i).getProject_id() == project_id){

                    }
                }*/

                try {

                    JSONArray jArray = new JSONArray();// /ItemDetail jsonArray
                    for (int i = 0; i < tasklist.size(); i++) {
                        JSONObject jGroup = new JSONObject();// /sub Object

                        try {
                            jGroup.put("project_id", tasklist.get(i).getProject_id());
                            jGroup.put("start_date", tasklist.get(i).getStart_date());
                            jGroup.put("end_date", tasklist.get(i).getEnd_date());
                            jGroup.put("day_1", tasklist.get(i).getDay_1());
                            jGroup.put("day_2", tasklist.get(i).getDay_2());
                            jGroup.put("day_3", tasklist.get(i).getDay_3());
                            jGroup.put("day_4", tasklist.get(i).getDay_4());
                            jGroup.put("day_5", tasklist.get(i).getDay_5());
                            jGroup.put("day_6", tasklist.get(i).getDay_6());
                            jGroup.put("day_7", tasklist.get(i).getDay_7());

                            jArray.put(jGroup);
                            sendProjectDetailsToServer(jArray.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }


                    //  sendProjectDetailsToServer(jsonObject.toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void sendProjectDetailsToServer(final String timeSheet) {
        if (NetworkConnection.isNetworkAvailable(ProjectBackUp2.this)) {
            Utils.showProgressDialog(ProjectBackUp2.this, progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.ADD_TASK4, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.ADD_TASK4,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
                                        finish();

                                    } else {
                                        Utils.showSnackBar(ProjectBackUp2.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(ProjectBackUp2.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(ProjectBackUp2.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog(Log.ERROR, AppConfigTags.ERROR, new String(response.data), true);
                            }
                            Utils.showSnackBar(ProjectBackUp2.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.TIMESHEET, timeSheet);
                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put(AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref(ProjectBackUp2.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest1, 60);
        } else {
            Utils.showSnackBar(this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }
}
