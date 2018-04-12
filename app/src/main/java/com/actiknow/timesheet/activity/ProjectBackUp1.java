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

public class ProjectBackUp1 extends AppCompatActivity {
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

    AppDetailsPref appDetailsPref=AppDetailsPref.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up1);
        this.savedInstanceState = savedInstanceState;
        initView();
        initData();
        setData();
        initListener();

        //    recommendedJobList ();
    }

    private void setData() {

        try {
            projects = getIntent().getExtras().getString("allProjects");
            position = getIntent().getExtras().getInt("position", 0);
            JSONArray jsonArray = new JSONArray(projects);
            array_length = jsonArray.length();
            if (jsonArray.length() > 0) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    if (j == position) {
                        tvProjectName.setText(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                        project_id = jsonObject.getInt(AppConfigTags.PROJECT_ID);

                        JSONArray jsonArrayHour = jsonObject.getJSONArray(AppConfigTags.HOURS);
                        Log.e("jsonArrayHour",jsonArrayHour.toString());
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
        ivNextProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (array_length - 1 > position) {
                    position = position + 1;
                    try {
                        JSONArray jsonArray = new JSONArray(projects);
                        JSONObject jsonObject = jsonArray.getJSONObject(position);
                        tvProjectName.setText(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                        project_id = jsonObject.getInt(AppConfigTags.PROJECT_ID);

                        for (Task t : tasklist) {
                            if (t.getProject_id() == project_id && t.getDate().equalsIgnoreCase(tvDate1.getText().toString())) {
                                etMondayhour.setText("" + t.getNo_of_hrs());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        progressDialog = new ProgressDialog(ProjectBackUp1.this);
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

       /* etMondayhour.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("Value", project_id + "-" + s.toString() + "-" + tvDate6.getText().toString());
                if (s.toString().length() > 0) {

                    tasklist.add(new Task(project_id, Integer.valueOf(s.toString()), tvDate1.getText().toString()));
                    Log.e("TaskList", "" + tasklist.get(0).getNo_of_hrs());
                } else {

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        etTueshour.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.e("Value", s.toString()+"-"+tvDate6.getText().toString());
                // Task task = new Task();
                // task.setDate(tvDate1.getText().toString());
                tasklist.add(new Task(project_id, Integer.valueOf(s.toString()), tvDate2.getText().toString()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        etWednesdayHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.e("Value", s.toString()+"-"+tvDate6.getText().toString());
                // Task task = new Task();
                // task.setDate(tvDate1.getText().toString());
                tasklist.add(new Task(project_id, Integer.valueOf(s.toString()), tvDate3.getText().toString()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        etThursdayhour.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.e("Value", s.toString()+"-"+tvDate6.getText().toString());
                // Task task = new Task();
                // task.setDate(tvDate1.getText().toString());
                tasklist.add(new Task(project_id, Integer.valueOf(s.toString()), tvDate4.getText().toString()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        etFridayhour.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.e("Value", s.toString()+"-"+tvDate6.getText().toString());
                // Task task = new Task();
                // task.setDate(tvDate1.getText().toString());
                tasklist.add(new Task(project_id, Integer.valueOf(s.toString()), tvDate5.getText().toString()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        etSaturdayhour.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.e("Value", s.toString()+"-"+tvDate6.getText().toString());
                // Task task = new Task();
                // task.setDate(tvDate1.getText().toString());
                tasklist.add(new Task(project_id, Integer.valueOf(s.toString()), tvDate6.getText().toString()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        etSundayhour.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.e("Value", s.toString()+"-"+tvDate6.getText().toString());
                // Task task = new Task();
                // task.setDate(tvDate1.getText().toString());
                tasklist.add(new Task(project_id, Integer.valueOf(s.toString()), tvDate7.getText().toString()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });
*/

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


                    jsonObject.put("project_id", project_id);
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

                    sendProjectDetailsToServer(jsonObject.toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void sendProjectDetailsToServer(final String timeSheet) {
        if (NetworkConnection.isNetworkAvailable(ProjectBackUp1.this)) {
            Utils.showProgressDialog(ProjectBackUp1.this, progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
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
                                        finish();

                                    } else {
                                        Utils.showSnackBar(ProjectBackUp1.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(ProjectBackUp1.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(ProjectBackUp1.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(ProjectBackUp1.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                    params.put(AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref(ProjectBackUp1.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
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
