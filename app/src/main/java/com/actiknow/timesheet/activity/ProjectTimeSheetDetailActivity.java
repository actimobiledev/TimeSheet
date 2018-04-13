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
import com.afollestad.materialdialogs.MaterialDialog;
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
 * Created by l on 12/04/2018 rahul jain.
 */

public class ProjectTimeSheetDetailActivity extends AppCompatActivity {
    Bundle savedInstanceState;

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
    ProgressDialog progressDialog;
    CoordinatorLayout clMain;

    String projects = "";
    int position = 0;
    int project_id = 0;
    int array_length = 0;
    int i = 0;
    ArrayList<Task> tasklist = new ArrayList<>();

    String day1 = "0";
    String day2 = "0";
    String day3 = "0";
    String day4 = "0";
    String day5 = "0";
    String day6 = "0";
    String day7 = "0";

    ArrayList<String> projectList = new ArrayList<>();
    ArrayList<Integer> projectID = new ArrayList<>();
    int projectdialogid = 0;
    int projectId;
    LinearLayout llPrevious;

    AppDetailsPref appDetailsPref = AppDetailsPref.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_sheet_detail);
        this.savedInstanceState = savedInstanceState;
        initView();
        initData();
        setData(20);
        initListener();

    }
    @Override
    public void onResume() {
        super.onResume();

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
        llPrevious=(LinearLayout)findViewById(R.id.llPrevious);

    }


    private void initListener() {

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


        tvProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(ProjectTimeSheetDetailActivity.this)
                        .title("Project List")
                        .items(projectList)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                projectId = projectID.get(which);
                                tvProjectName.setText(text);
                                if (projectdialogid == 0) {
                                    saveDataLocally(project_id, 1);
                                } else {
                                    saveDataLocally(projectdialogid, 1);
                                }
                                projectdialogid = projectId;
                                Log.e("item number", "test-" + projectId);
                            }
                        })
                        .show();

            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataLocally(project_id,20);

            }
        });
        llPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProjectTimeSheetDetailActivity.this,PreviousWeekProjectDetailActivity.class);
                if(projectdialogid == 0){
                    intent.putExtra("id",project_id);
                }else {
                    intent.putExtra("id",projectId);
                }
                intent.putExtra("project_name",tvProjectName.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void initData() {
        progressDialog = new ProgressDialog(ProjectTimeSheetDetailActivity.this);
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



    }


    private void setData(int project_id2) {
        Log.e("project_id", "setdata-" + project_id2);
        projectList.clear();

        try {
            projects = getIntent().getExtras().getString("allProjects");
            position = getIntent().getExtras().getInt("position", 0);
            JSONArray jsonArray = new JSONArray(projects);
            array_length = jsonArray.length();
            if (jsonArray.length() > 0) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    projectList.add(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                    projectID.add(jsonObject.getInt(AppConfigTags.PROJECT_ID));


                    if (j == position && project_id2 == 20) {
                        tvProjectName.setText(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                        project_id = jsonObject.getInt(AppConfigTags.PROJECT_ID);

                        JSONArray jsonArrayHour = jsonObject.getJSONArray(AppConfigTags.HOURS);
                        Log.e("jsonArrayHour", jsonArrayHour.toString());
                        for (int i = 0; i < jsonArrayHour.length(); i++) {
                            JSONObject jsonObject2 = jsonArrayHour.getJSONObject(i);

                            String Date=Utils.dateFormat(jsonObject2.getString(AppConfigTags.DATE));
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


                    if (project_id2 == jsonObject.getInt(AppConfigTags.PROJECT_ID)) {

                        JSONArray jsonArrayHour2 = jsonObject.getJSONArray(AppConfigTags.HOURS);
                        Log.e("jsonArrayHour4", jsonArrayHour2.toString());

                        if (jsonArrayHour2.length() == 0) {
                            etSundayhour.setText("0");
                            etMondayhour.setText("0");
                            etTueshour.setText("0");
                            etWednesdayHour.setText("0");
                            etThursdayhour.setText("0");
                            etFridayhour.setText("0");
                            etSaturdayhour.setText("0");
                        }
                        for (int i = 0; i < jsonArrayHour2.length(); i++) {
                            JSONObject jsonObject2 = jsonArrayHour2.getJSONObject(i);
                            String Date=Utils.dateFormat(jsonObject2.getString(AppConfigTags.DATE));

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
        }

    }


    private void saveDataLocally(int project_id3, int type1) {
        JSONObject jsonObject = new JSONObject();
        try {
            DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String inputDateStr = tvDate1.getText().toString();
            String inputDateStr2 = tvDate7.getText().toString();
            Date date = null;
            Date date1 = null;
            date = inputFormat.parse(inputDateStr);
            date1 = inputFormat.parse(inputDateStr2);
            String startDate = outputFormat.format(date);
            String endDate = outputFormat.format(date1);


            day1 = etMondayhour.getText().toString().trim();
            day2 = etTueshour.getText().toString().trim();
            day3 = etWednesdayHour.getText().toString().trim();
            day4 = etThursdayhour.getText().toString().trim();
            day5 = etFridayhour.getText().toString().trim();
            day6 = etSaturdayhour.getText().toString().trim();
            day7 = etSundayhour.getText().toString().trim();


            jsonObject.put("project_id", project_id3);
            jsonObject.put("start_date", startDate);
            jsonObject.put("end_date", endDate);
            jsonObject.put("day_1", day1.equalsIgnoreCase("") ? "0" : etMondayhour.getText().toString());
            jsonObject.put("day_2", day2.equalsIgnoreCase("") ? "0" : etTueshour.getText().toString());
            jsonObject.put("day_3", day3.equalsIgnoreCase("") ? "0" : etWednesdayHour.getText().toString());
            jsonObject.put("day_4", day4.equalsIgnoreCase("") ? "0" : etThursdayhour.getText().toString());
            jsonObject.put("day_5", day5.equalsIgnoreCase("") ? "0" : etFridayhour.getText().toString());
            jsonObject.put("day_6", day6.equalsIgnoreCase("") ? "0" : etSaturdayhour.getText().toString());
            jsonObject.put("day_7", day7.equalsIgnoreCase("") ? "0" : etSundayhour.getText().toString());


            Log.e("jsonObject", "" + jsonObject.toString());

            sendProjectDetailsToServer(jsonObject.toString(), type1, project_id3);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendProjectDetailsToServer(final String timeSheet, final int type1, final int project_id4) {
        if (NetworkConnection.isNetworkAvailable(ProjectTimeSheetDetailActivity.this)) {
            Utils.showProgressDialog(ProjectTimeSheetDetailActivity.this, progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.ADD_TASK, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.ADD_TASK,
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
                                        JSONArray jsonArray = jsonObj.getJSONArray(AppConfigTags.PROJECTS);

                                        // Log.e("projects",allClients);
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(j);

                                            int projectId5 = jsonObject.getInt(AppConfigTags.PROJECT_ID);

                                            JSONArray jsonArrayHour = jsonObject.getJSONArray(AppConfigTags.HOURS);
                                            Log.e("jsonArrayHour", jsonArrayHour.toString());
                                            for (int i = 0; i < jsonArrayHour.length(); i++) {
                                                JSONObject jsonObject2 = jsonArrayHour.getJSONObject(i);


                                                if (projectId == projectId5) {

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

                                            }


                                        }


                                        if (type1 == 1) {

                                        } else {
                                            finish();
                                        }

                                    } else {
                                        Utils.showSnackBar(ProjectTimeSheetDetailActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(ProjectTimeSheetDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(ProjectTimeSheetDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(ProjectTimeSheetDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                    params.put(AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref(ProjectTimeSheetDetailActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
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























   /* public void HourList(int project_id) {
        if (NetworkConnection.isNetworkAvailable(ProjectTimeSheetDetailActivity.this)) {
            projectList.clear();
            Utils.showProgressDialog(ProjectTimeSheetDetailActivity.this, progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, AppConfigTags.URL, AppConfigURL.PROJECTS, true);
            StringRequest strRequest = new StringRequest(Request.Method.GET, AppConfigURL.PROJECTS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean is_error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!is_error) {
                                        JSONArray jsonArray = jsonObj.getJSONArray(AppConfigTags.HOURS);

                                        // Log.e("projects",allClients);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            if (Utils.dateFormat(jsonObject.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate1.getText().toString())) {
                                                etMondayhour.setText(jsonObject.getString(AppConfigTags.hour));
                                            }
                                            if (Utils.dateFormat(jsonObject.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate2.getText().toString())) {
                                                etTueshour.setText(jsonObject.getString(AppConfigTags.hour));
                                            }
                                            if (Utils.dateFormat(jsonObject.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate3.getText().toString())) {
                                                etWednesdayHour.setText(jsonObject.getString(AppConfigTags.hour));
                                            }
                                            if (Utils.dateFormat(jsonObject.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate4.getText().toString())) {
                                                etThursdayhour.setText(jsonObject.getString(AppConfigTags.hour));
                                            }
                                            if (Utils.dateFormat(jsonObject.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate5.getText().toString())) {
                                                etFridayhour.setText(jsonObject.getString(AppConfigTags.hour));
                                            }
                                            if (Utils.dateFormat(jsonObject.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate6.getText().toString())) {
                                                etSaturdayhour.setText(jsonObject.getString(AppConfigTags.hour));
                                            }
                                            if (Utils.dateFormat(jsonObject.getString(AppConfigTags.DATE)).equalsIgnoreCase(tvDate7.getText().toString())) {
                                                etSundayhour.setText(jsonObject.getString(AppConfigTags.hour));
                                            }
                                        }



                                    } else {
                                        Utils.showSnackBar(ProjectTimeSheetDetailActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Utils.showSnackBar(ProjectTimeSheetDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);

                                }
                            } else {
                                Utils.showSnackBar(ProjectTimeSheetDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog(Log.ERROR, AppConfigTags.ERROR, new String(response.data), true);
                            }
                            Utils.showSnackBar(ProjectTimeSheetDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    AppDetailsPref appDetailsPref = AppDetailsPref.getInstance();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put(AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref(ProjectTimeSheetDetailActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest, 5);
        } else {
            Utils.showSnackBar(ProjectTimeSheetDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }*/


}
