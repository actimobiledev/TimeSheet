package com.actiknow.timesheet.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.adapter.ProjectAdapter;
import com.actiknow.timesheet.dialog.AddProjectDialogFragment;
import com.actiknow.timesheet.model.Project;
import com.actiknow.timesheet.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by l on 27/07/2017.
 */

public class ProjectActivity extends AppCompatActivity {
    Bundle savedInstanceState;
    RecyclerView rvProjectList;
    CoordinatorLayout clMain;
    TextView tvTitle;
    FloatingActionButton fabAddProject;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout rlBack;
    ProjectAdapter projectAdapter;
    ArrayList<Project> projectList = new ArrayList<>();

    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_project);
        this.savedInstanceState = savedInstanceState;
        initView ();
        initData ();
        initListener ();
    //    recommendedJobList ();
    }
    
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        rvProjectList = (RecyclerView) findViewById (R.id.rvProjectList);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        fabAddProject = (FloatingActionButton) findViewById(R.id.fabAddProject);
    }
    
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        fabAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                AddProjectDialogFragment fragment = AddProjectDialogFragment.newInstance ();
                fragment.show (ft, "test");
            }
        });


    }
    
    private void initData () {
        projectList.clear ();
        projectList.add (new Project (1, "HomeTrust", "This is the timesheet application for actinow employees", "3 hours"));
        projectList.add (new Project (2, "ActiProject", "This is the timesheet application for actinow employees", "3 hours"));
        projectList.add (new Project (3, "P&K", "This is the timesheet application for actinow employees", "3 hours"));
        projectAdapter = new ProjectAdapter(this, projectList);
        rvProjectList.setAdapter (projectAdapter);
        rvProjectList.setHasFixedSize (true);
        rvProjectList.addItemDecoration (new SimpleDividerItemDecoration(this));
        rvProjectList.setLayoutManager (new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvProjectList.setItemAnimator (new DefaultItemAnimator());
    }


    
    /*public void recommendedJobList () {
        if (NetworkConnection.isNetworkAvailable (ProjectActivity.this)) {
            jobDescriptionList.clear ();
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_RECOMMENDED, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_RECOMMENDED,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        JSONArray jsonArrayJobs = jsonObj.getJSONArray (AppConfigTags.JOBS);
                                        for (int i = 0; i < jsonArrayJobs.length (); i++) {
                                            JSONObject jsonObjectDescription = jsonArrayJobs.getJSONObject (i);
                                            JobDescription jobDescription = new JobDescription (
                                                    jsonObjectDescription.getInt (AppConfigTags.JOB_ID),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_TITLE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_DESCRIPTION),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_MIN_EXPERIENCE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_MAX_EXPERIENCE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_SKILLS),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_COMPANY),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_LOCATION),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_MIN_SALARY),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_MAX_EXPERIENCE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_POSTED_AT),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_TYPE),
                                                    jsonObjectDescription.getString (AppConfigTags.JOB_EXPIRES_IN),
                                                    jsonObjectDescription.getBoolean (AppConfigTags.JOB_BOOKMARKED)
                                            );
                                            jobDescriptionList.add (i, jobDescription);
                                        }
                                        
                                        
                                        jobDescriptionAdapter.notifyDataSetChanged ();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showSnackBar (ProjectActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    
                                }
                            } else {
                                Utils.showSnackBar (ProjectActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showSnackBar (ProjectActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (ProjectActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
            Utils.showSnackBar (ProjectActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }*/
}
