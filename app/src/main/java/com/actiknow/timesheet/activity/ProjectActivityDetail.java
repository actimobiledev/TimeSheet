package com.actiknow.timesheet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by l on 27/07/2017.
 */

public class ProjectActivityDetail extends AppCompatActivity {
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

    String projects = "";
    int position = 0;
    int project_id = 0;
    int array_length = 0;
    int i = 0;
    ArrayList<Task>tasklist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        this.savedInstanceState = savedInstanceState;
        initView();
        initData();
        setData();
        initListener();

        //    recommendedJobList ();
    }

    private void setData() {
        /*String projects = getIntent().getExtras().getString("allClients");
        int position=getIntent().getExtras().getInt("position", 0);
        Log.e("Clients",""+projects+"-"+position);*/
        try {
            projects = getIntent().getExtras().getString("allProjects");
            position=getIntent().getExtras().getInt("position", 0);
            JSONArray jsonArray = new JSONArray(projects);
            array_length = jsonArray.length();
            if (jsonArray.length() > 0) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    if (j==position){
                        tvProjectName.setText(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                        project_id = jsonObject.getInt(AppConfigTags.PROJECT_ID);
                    }


                  //  tv(jsonObject.getString(AppConfigTags.CLIENT_NAME));
                   // clientID.add(jsonObject.getInt(AppConfigTags.CLIENT_ID));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        //   projectList();
        // put your code here...

    }


    private void initView() {
        rlBack = (RelativeLayout)findViewById( R.id.rlBack );
        ivBack = (ImageView)findViewById( R.id.ivBack );
        tvTitle = (TextView)findViewById( R.id.tvTitle );
        rlList = (RelativeLayout)findViewById( R.id.rlList );
        llProjectName = (LinearLayout)findViewById( R.id.llProjectName );
        ivPreProject = (ImageView)findViewById( R.id.ivPreProject );
        ivNextProject = (ImageView)findViewById( R.id.ivNextProject );
        llDay1 = (LinearLayout)findViewById( R.id.llDay1 );
        tvDate1 = (TextView)findViewById( R.id.tvDate1 );
        etMondayhour = (EditText)findViewById( R.id.etMondayhour);
        llDay2 = (LinearLayout)findViewById( R.id.llDay2 );
        tvDate2 = (TextView)findViewById( R.id.tvDate2 );
        etTueshour = (EditText)findViewById( R.id.etTueshour );
        llDay3 = (LinearLayout)findViewById( R.id.llDay3 );
        tvDate3 = (TextView)findViewById( R.id.tvDate3 );
        etWednesdayHour = (EditText)findViewById( R.id.etWednesdayHour );
        llDay4 = (LinearLayout)findViewById( R.id.llDay4 );
        tvDate4 = (TextView)findViewById( R.id.tvDate4 );
        etThursdayhour = (EditText)findViewById( R.id.etThursdayhour );
        llDay5 = (LinearLayout)findViewById( R.id.llDay5 );
        tvDate5 = (TextView)findViewById( R.id.tvDate5 );
        etFridayhour = (EditText)findViewById( R.id.etFridayhour );
        llDay6 = (LinearLayout)findViewById( R.id.llDay6 );
        tvDate6 = (TextView)findViewById( R.id.tvDate6 );
        etSaturdayhour = (EditText)findViewById( R.id.etSaturdayhour );
        llDay7 = (LinearLayout)findViewById( R.id.llDay7 );
        tvDate7 = (TextView)findViewById( R.id.tvDate7 );
        etSundayhour = (EditText)findViewById( R.id.etSundayhour );
        tvSubmit = (TextView)findViewById( R.id.tvSubmit );
        tvProjectName = (TextView)findViewById( R.id.tvProjectName );
        ivPreProject = (ImageView)findViewById(R.id.ivPreProject);
        ivNextProject = (ImageView)findViewById(R.id.ivNextProject);

    }


    private void initListener() {
        ivNextProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (array_length-1 > position) {
                    position = position+1;
                    try {
                        JSONArray jsonArray = new JSONArray(projects);
                        JSONObject jsonObject = jsonArray.getJSONObject(position);
                        tvProjectName.setText(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                        project_id = jsonObject.getInt(AppConfigTags.PROJECT_ID);
                        /*for(Task t : tasklist){
                            if(t.getProject_id() == project_id && t.getDate().equalsIgnoreCase(tvDate1.getText().toString())){
                                etMondayhour.setText(""+t.getNo_of_hrs());
                            }
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        ivPreProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position > 0){
                    position = position-1;
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

        Calendar c = Calendar.getInstance(); // Set the calendar to Sunday of the current week
        c.set(Calendar.DAY_OF_WEEK, c.MONDAY); // Print dates of the current week starting on Sunday
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 1; i < 8; i++) {
            switch (i){
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

        etMondayhour.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("Value", project_id+"-"+s.toString()+"-"+tvDate6.getText().toString());
                if(s.toString().length() > 0) {
                    tasklist.add(new Task(project_id, Integer.valueOf(s.toString()), tvDate1.getText().toString()));
                    Log.e("TaskList", "" + tasklist.get(0).getNo_of_hrs());
                }else{

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


    }

    /*public void projectList() {
        if (NetworkConnection.isNetworkAvailable(ProjectActivityDetail.this)) {
            projectList.clear();
            Utils.showProgressDialog(ProjectActivityDetail.this, progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
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
                                        JSONArray jsonArray = jsonObj.getJSONArray(AppConfigTags.PROJECTS);
                                        allClients = jsonObj.getJSONArray(AppConfigTags.CLIENTS).toString();
                                        // Log.e("projects",allClients);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            Project project = new Project(
                                                    jsonObject.getInt(AppConfigTags.PROJECT_ID),
                                                    jsonObject.getString(AppConfigTags.PROJECT_TITLE),
                                                    jsonObject.getString(AppConfigTags.PROJECT_DESCRIPTION),
                                                    jsonObject.getString(AppConfigTags.PROJECT_ALLOTED_HOUR)


                                            );
                                            projectList.add(i, project);
                                        }


                                        projectAdapter.notifyDataSetChanged();
                                    } else {
                                        Utils.showSnackBar(ProjectActivityDetail.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Utils.showSnackBar(ProjectActivityDetail.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);

                                }
                            } else {
                                Utils.showSnackBar(ProjectActivityDetail.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(ProjectActivityDetail.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                    params.put(AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref(ProjectActivityDetail.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest, 5);
        } else {
            Utils.showSnackBar(ProjectActivityDetail.this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }*/

  /*  public interface MyDialogCloseListener2 {
        public void handleDialogClose(DialogInterface dialog);
    }*/
}
