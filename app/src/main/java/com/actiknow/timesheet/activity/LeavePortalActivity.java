package com.actiknow.timesheet.activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.adapter.LeaveTypeAdapter;
import com.actiknow.timesheet.dialog.ApplyLeaveDialogFragment;
import com.actiknow.timesheet.dialog.LeavesListDialogFragment;
import com.actiknow.timesheet.model.LeaveType;
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

public class LeavePortalActivity extends AppCompatActivity {
    RecyclerView rvLeave;
    CoordinatorLayout clMain;
    TextView tvTitle;
    RelativeLayout rlBack;
    RelativeLayout rlMyLeaves;
    RelativeLayout rlApplyLeave;
    LeaveTypeAdapter leaveTypeAdapter;
    ArrayList<LeaveType> leaveTypeList = new ArrayList<> ();
    ProgressDialog progressDialog;
    AppDetailsPref appDetailsPref;
    
    String leaves_json = "";
    
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_leave_portal);
        initView ();
        initData ();
        initAdapter ();
        initListener ();
        getLeavePortal ();
        
    }
    
    @Override
    public void onResume () {
        super.onResume ();
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        rvLeave = (RecyclerView) findViewById (R.id.rvLeave);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        rlApplyLeave = (RelativeLayout) findViewById (R.id.rlApplyLeave);
        rlMyLeaves = (RelativeLayout) findViewById (R.id.rlMyLeaves);
    }
    
    private void initAdapter () {
        rvLeave.setAdapter (leaveTypeAdapter);
        rvLeave.setHasFixedSize (true);
        rvLeave.setHasFixedSize (true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
        rvLeave.setLayoutManager (layoutManager);
//        rvProjectList.addItemDecoration (new RecyclerViewMargin (
//                (int) Utils.pxFromDp (this, 16),
//                (int) Utils.pxFromDp (this, 16),
//                (int) Utils.pxFromDp (this, 16),
//                (int) Utils.pxFromDp (this, 16),
//                1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    
        leaveTypeAdapter.SetOnItemClickListener (new LeaveTypeAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                LeaveType leaveType = leaveTypeList.get (position);
                FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                LeavesListDialogFragment fragment = LeavesListDialogFragment.newInstance (leaves_json, leaveType.getType_id ());
                fragment.setOnDialogResultListener (new LeavesListDialogFragment.OnDialogResultListener () {
                    @Override
                    public void onPositiveResult () {
                    }
    
                    @Override
                    public void onNegativeResult () {
                    }
                });
                fragment.show (ft, AppConfigTags.LEAVES);
            }
        });
    
        rlMyLeaves.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                LeavesListDialogFragment fragment = LeavesListDialogFragment.newInstance (leaves_json, 0);
                fragment.setOnDialogResultListener (new LeavesListDialogFragment.OnDialogResultListener () {
                    @Override
                    public void onPositiveResult () {
                    }
                
                    @Override
                    public void onNegativeResult () {
                    }
                });
                fragment.show (ft, AppConfigTags.LEAVES);
            }
        });
    
        rlApplyLeave.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                ApplyLeaveDialogFragment fragment = ApplyLeaveDialogFragment.newInstance ();
                fragment.setOnDialogResultListener (new ApplyLeaveDialogFragment.OnDialogResultListener () {
                    @Override
                    public void onPositiveResult () {
                    }
                
                    @Override
                    public void onNegativeResult () {
                    }
                });
                fragment.show (ft, AppConfigTags.LEAVES);
            }
        });
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, clMain);
        appDetailsPref = AppDetailsPref.getInstance ();
        progressDialog = new ProgressDialog (this);
        leaveTypeAdapter = new LeaveTypeAdapter (this, leaveTypeList);
    }
    
    public void getLeavePortal () {
        if (NetworkConnection.isNetworkAvailable (LeavePortalActivity.this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_LEAVE_PORTAL, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_LEAVE_PORTAL,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    leaveTypeList.clear ();
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        leaves_json = jsonObj.getJSONArray (AppConfigTags.LEAVES).toString ();
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.TYPES);
                                        appDetailsPref.putStringPref (LeavePortalActivity.this, AppDetailsPref.LEAVE_TYPES, jsonArray.toString ());
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (i);
                                            if (jsonObject.getDouble (AppConfigTags.AVAILED) != 0 || jsonObject.getDouble (AppConfigTags.REMAINING) != 0) {
                                                leaveTypeList.add (new LeaveType (
                                                        jsonObject.getInt (AppConfigTags.TYPE_ID),
                                                        jsonObject.getString (AppConfigTags.TYPE_NAME),
                                                        jsonObject.getString (AppConfigTags.TYPE_STATUS),
                                                        jsonObject.getString (AppConfigTags.TOTAL),
                                                        jsonObject.getString (AppConfigTags.AVAILED),
                                                        jsonObject.getString (AppConfigTags.REMAINING)
                                                ));
                                            }
                                        }
                                        leaveTypeAdapter.notifyDataSetChanged ();
                                    } else {
                                        Utils.showSnackBar (LeavePortalActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showSnackBar (LeavePortalActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                }
                            } else {
                                Utils.showSnackBar (LeavePortalActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (LeavePortalActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (LeavePortalActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
            Utils.showSnackBar (LeavePortalActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
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