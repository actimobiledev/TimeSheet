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
import com.actiknow.timesheet.model.Project;
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
import com.android.volley.Response;
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
import java.util.List;
import java.util.Map;

/**
 * Created by l on 27/07/2017.
 */

public class PreviousWeekProjectDetailActivity extends AppCompatActivity {
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
    int projectId;
    int projectdialogid = 0;

    AppDetailsPref appDetailsPref=AppDetailsPref.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_week_timesheet);
        this.savedInstanceState = savedInstanceState;
        initView();
        initData();
        initListener();
      //  PreviousDetail(0);

        //    recommendedJobList ();
    }


    @Override
    public void onResume() {
        super.onResume();
        //   projectList();
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
                new MaterialDialog.Builder(PreviousWeekProjectDetailActivity.this)
                        .title("Project List")
                        .items(projectList)

                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                projectId = projectID.get(which);
                                tvProjectName.setText(text);
                               // PreviousDetail(projectId);

                                if (projectId == position) {
                                    PreviousDetail(position);
                                } else {
                                    PreviousDetail(projectId);
                                }

                                //setData(projectId);
                                //saveDataLocally(projectId);
                                Log.e("item number", "test-" + projectId);
                            }
                        })

                        .show();

            }
        });


    }

    private void initData() {
        Calendar c = Calendar.getInstance(); // Set the calendar to Sunday of the current week
        c.set(Calendar.DAY_OF_WEEK, c.MONDAY); // Print dates of the current week starting on Sunday
        c.add(Calendar.DATE, -7);
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

        progressDialog = new ProgressDialog(PreviousWeekProjectDetailActivity.this);
        projects = getIntent().getExtras().getString("project_name");
        position = getIntent().getExtras().getInt("id", 0);
        Log.e("id",""+position);
        tvProjectName.setText(projects);
        PreviousDetail(position);

    }











    private void PreviousDetail(final int id) {
        projectList.clear();
        projectID.clear();
        if (NetworkConnection.isNetworkAvailable(PreviousWeekProjectDetailActivity.this)) {
            Utils.showProgressDialog(PreviousWeekProjectDetailActivity.this, progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.PREVIOUS_WEEK, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.GET, AppConfigURL.PREVIOUS_WEEK,
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
                                        JSONArray jsonArray = jsonObj.getJSONArray(AppConfigTags.PREVIOUS_WEEK);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                                            projectList.add(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                                            projectID.add(jsonObject.getInt(AppConfigTags.PROJECT_ID));

                                            if (id == jsonObject.getInt(AppConfigTags.PROJECT_ID)) {
                                                Log.d("id1222","123----"+jsonObject.getInt(AppConfigTags.PROJECT_ID));
                                                etMondayhour.setText(jsonObject.getString(AppConfigTags.DAY_1));
                                                etTueshour.setText(jsonObject.getString(AppConfigTags.DAY_2));
                                                etWednesdayHour.setText(jsonObject.getString(AppConfigTags.DAY_3));
                                                etThursdayhour.setText(jsonObject.getString(AppConfigTags.DAY_4));
                                                etFridayhour.setText(jsonObject.getString(AppConfigTags.DAY_5));
                                                etSaturdayhour.setText(jsonObject.getString(AppConfigTags.DAY_6));
                                                etSundayhour.setText(jsonObject.getString(AppConfigTags.DAY_7));
                                            }
                                        }
                                    } else {
                                        Utils.showSnackBar(PreviousWeekProjectDetailActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(PreviousWeekProjectDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(PreviousWeekProjectDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(PreviousWeekProjectDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss();
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
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put(AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref(PreviousWeekProjectDetailActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
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
        if (NetworkConnection.isNetworkAvailable(PreviousWeekProjectDetailActivity.this)) {
            projectList.clear();
            Utils.showProgressDialog(PreviousWeekProjectDetailActivity.this, progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
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
                                        Utils.showSnackBar(PreviousWeekProjectDetailActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Utils.showSnackBar(PreviousWeekProjectDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);

                                }
                            } else {
                                Utils.showSnackBar(PreviousWeekProjectDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(PreviousWeekProjectDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                    params.put(AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref(PreviousWeekProjectDetailActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest, 5);
        } else {
            Utils.showSnackBar(PreviousWeekProjectDetailActivity.this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
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
