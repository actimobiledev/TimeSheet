package com.actiknow.timesheet.activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.adapter.ProjectAdapter;
import com.actiknow.timesheet.dialog.AddProjectDialogFragment;
import com.actiknow.timesheet.model.Project;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppConfigURL;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Constants;
import com.actiknow.timesheet.utils.NetworkConnection;
import com.actiknow.timesheet.utils.SimpleDividerItemDecoration;
import com.actiknow.timesheet.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by l on 27/07/2017.
 */

public class ProjectActivityDetail extends AppCompatActivity {
    Bundle savedInstanceState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        this.savedInstanceState = savedInstanceState;
        initView();
        initData();
        initListener();
        //    recommendedJobList ();
    }

    @Override
    public void onResume() {
        super.onResume();
        //   projectList();
        // put your code here...

    }


    private void initView() {

    }


    private void initListener() {
       /* rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });*/


    }

    private void initData() {

        Calendar c = Calendar.getInstance(); // Set the calendar to Sunday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // Print dates of the current week starting on Sunday
        DateFormat df = new SimpleDateFormat("EEE dd/MM/yyyy");
        for (int i = 0; i < 7; i++) {
            System.out.println(df.format(c.getTime()));
            c.add(Calendar.DATE, 1);
        }


/*
        Calendar calendar = new GregorianCalendar(2008, 01, 01); // Note that Month value is 0-based. e.g., 0 for January.
        int reslut = calendar.get(Calendar.DAY_OF_WEEK);
        switch (reslut) {
            case Calendar.MONDAY:
                System.out.println("It's Monday !");
                break;
            case Calendar.TUESDAY:
                System.out.println("It's Monday2 !");
                break;
            case Calendar.WEDNESDAY:
                System.out.println("It's Monday3 !");
                break;
            case Calendar.THURSDAY:
                System.out.println("It's Monday4 !");
                break;
            case Calendar.FRIDAY:
                System.out.println("It's Monday5 !");
                break;
            case Calendar.SATURDAY:
                System.out.println("It's Monday6 !");
                break;
        }*/

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
                                        // Log.e("clients",allClients);
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
