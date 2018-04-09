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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by l on 27/07/2017.
 */

public class ProjectActivity extends AppCompatActivity {
    Bundle savedInstanceState;
    RecyclerView rvProjectList;
    CoordinatorLayout clMain;
    TextView tvTitle;
    FloatingActionButton fabAddProject;

    RelativeLayout rlBack;
    ProjectAdapter projectAdapter;
    ArrayList<Project> projectList = new ArrayList<>();
    ProgressDialog progressDialog;
    String allClients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        this.savedInstanceState = savedInstanceState;
        initView();
        initData();
        initListener();
        //    recommendedJobList ();
    }

    @Override
    public void onResume() {
        super.onResume();
        projectList();
        // put your code here...

    }


    private void initView() {
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        rvProjectList = (RecyclerView) findViewById(R.id.rvProjectList);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        fabAddProject = (FloatingActionButton) findViewById(R.id.fabAddProject);
    }


    private void initListener() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        fabAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddProjectDialogFragment fragment = AddProjectDialogFragment.newInstance(allClients);

                fragment.setDismissListener(new MyDialogCloseListener2() {
                    @Override
                    public void handleDialogClose(DialogInterface dialog) {
                        projectList();
                    }
                });
                fragment.show(ft, "test");
            }
        });

       /* projectAdapter.SetOnItemClickListener(new ProjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(ProjectActivity.this, ProjectActivityDetail.class);
                intent.putExtra("allClients",allClients);
                intent.putExtra("position",position);
                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent);


            }
        });*/


    }

    private void initData() {
        projectList.clear();
        progressDialog = new ProgressDialog(this);
     /*   projectList.add (new Project (1, "HomeTrust", "This is the timesheet application for actinow employees", "3 hours"));
        projectList.add (new Project (2, "ActiProject", "This is the timesheet application for actinow employees", "3 hours"));
        projectList.add (new Project (3, "P&K", "This is the timesheet application for actinow employees", "3 hours"));*/
        projectAdapter = new ProjectAdapter(this, projectList);
        rvProjectList.setAdapter(projectAdapter);
        rvProjectList.setHasFixedSize(true);
        rvProjectList.addItemDecoration(new SimpleDividerItemDecoration(this));
        rvProjectList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvProjectList.setItemAnimator(new DefaultItemAnimator());
    }


    public void projectList() {
        if (NetworkConnection.isNetworkAvailable(ProjectActivity.this)) {
            projectList.clear();
            Utils.showProgressDialog(ProjectActivity.this, progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
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
                                        Utils.showSnackBar(ProjectActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Utils.showSnackBar(ProjectActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);

                                }
                            } else {
                                Utils.showSnackBar(ProjectActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(ProjectActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                    params.put(AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref(ProjectActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest, 5);
        } else {
            Utils.showSnackBar(ProjectActivity.this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }

    public interface MyDialogCloseListener2 {
        public void handleDialogClose(DialogInterface dialog);
    }
}
