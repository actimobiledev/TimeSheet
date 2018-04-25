package com.actiknow.timesheet.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.adapter.MyEmployeeAdapter;
import com.actiknow.timesheet.model.MyEmployee;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppConfigURL;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Constants;
import com.actiknow.timesheet.utils.NetworkConnection;
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

public class MyEmployeesActivity extends AppCompatActivity {
    RecyclerView rvEmployees;
    CoordinatorLayout clMain;
    TextView tvTitle;
    FloatingActionButton fabAddEmployee;
    
    RelativeLayout rlBack;
    RelativeLayout rlNoResultFound;
    MyEmployeeAdapter myEmployeeAdapter;
    ArrayList<MyEmployee> myEmployeesList = new ArrayList<> ();
    ProgressDialog progressDialog;
    AppDetailsPref appDetailsPref;
    
    SwipeRefreshLayout swipeRefreshLayout;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_my_employees);
        initView ();
        initData ();
        initAdapter ();
        initListener ();
        getEmployeeList ();
    }
    
    @Override
    public void onResume () {
        super.onResume ();
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        rvEmployees = (RecyclerView) findViewById (R.id.rvEmployees);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        rlNoResultFound = (RelativeLayout) findViewById (R.id.rlNoResultFound);
        fabAddEmployee = (FloatingActionButton) findViewById (R.id.fabAddEmployee);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swipe_refresh_layout);
    }
    
    private void initAdapter () {
        rvEmployees.setAdapter (myEmployeeAdapter);
        rvEmployees.setHasFixedSize (true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false);
        rvEmployees.setLayoutManager (linearLayoutManager);
//        rvProjectList.addItemDecoration (new RecyclerViewMargin (
//                (int) Utils.pxFromDp (this, 16),
//                (int) Utils.pxFromDp (this, 16),
//                (int) Utils.pxFromDp (this, 16),
//                (int) Utils.pxFromDp (this, 16),
//                1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        rvEmployees.addItemDecoration (
                new DividerItemDecoration (this, linearLayoutManager.getOrientation ()) {
                    @Override
                    public void getItemOffsets (Rect outRect, View view, RecyclerView
                            parent, RecyclerView.State state) {
                        int position = parent.getChildAdapterPosition (view);
                        // hide the divider for the last child
                        if (position == parent.getAdapter ().getItemCount () - 1) {
                            outRect.setEmpty ();
                        } else {
                            super.getItemOffsets (outRect, view, parent, state);
                        }
                    }
                }
        );
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        
        fabAddEmployee.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent (MyEmployeesActivity.this, AddProjectActivity.class);
                startActivity (intent);
                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        
        myEmployeeAdapter.SetOnItemClickListener (new MyEmployeeAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
            }
        });
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh () {
                getEmployeeList ();
            }
        });
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, clMain);
        appDetailsPref = AppDetailsPref.getInstance ();
        swipeRefreshLayout.setRefreshing (true);
        myEmployeeAdapter = new MyEmployeeAdapter (this, myEmployeesList);
    }
    
    public void getEmployeeList () {
        if (NetworkConnection.isNetworkAvailable (MyEmployeesActivity.this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.MY_EMPLOYEES, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.MY_EMPLOYEES,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    myEmployeesList.clear ();
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.EMPLOYEES);
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (i);
                                            myEmployeesList.add (i, new MyEmployee (
                                                    jsonObject.getInt (AppConfigTags.EMPLOYEE_ID),
                                                    jsonObj.getString (AppConfigTags.EMPLOYEE_NAME),
                                                    jsonObj.getJSONArray (AppConfigTags.TOTAL).toString (),
                                                    jsonObj.getJSONArray (AppConfigTags.PROJECTS).toString ()
                                            ));
                                        }
                                        
                                        myEmployeeAdapter.notifyDataSetChanged ();
                                        
                                        if (jsonArray.length () > 0) {
                                            rlNoResultFound.setVisibility (View.GONE);
                                        } else {
                                            rlNoResultFound.setVisibility (View.VISIBLE);
                                        }
                                    } else {
                                        Utils.showSnackBar (MyEmployeesActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showSnackBar (MyEmployeesActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                }
                            } else {
                                Utils.showSnackBar (MyEmployeesActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            swipeRefreshLayout.setRefreshing (false);
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            swipeRefreshLayout.setRefreshing (false);
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showSnackBar (MyEmployeesActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                    AppDetailsPref appDetailsPref = AppDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (MyEmployeesActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
            swipeRefreshLayout.setRefreshing (false);
            Utils.showSnackBar (MyEmployeesActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
}