package com.actiknow.timesheet.activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.adapter.Employee2Adapter;
import com.actiknow.timesheet.dialog.EmployeeListDialogFragment;
import com.actiknow.timesheet.model.Employee2;
import com.actiknow.timesheet.model.Role;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppConfigURL;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Constants;
import com.actiknow.timesheet.utils.NetworkConnection;
import com.actiknow.timesheet.utils.SetTypeFace;
import com.actiknow.timesheet.utils.Utils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ProjectDetailActivity extends AppCompatActivity {
    
    Bundle savedInstanceState;
    String projects;
    int id;
    int project_id = 0;
    CoordinatorLayout clMain;
    List<Employee2> employee2List = new ArrayList<> ();
    Employee2Adapter employee2Adapter;
    AppDetailsPref appDetailsPref;
    ProgressDialog progressDialog;
    List<String> roleTitleList = new ArrayList<> ();
    List<Role> roleList = new ArrayList<Role> ();
    int role_id;
    int employee_id = 0;
    FloatingActionButton fabAddEmployee;
    private RelativeLayout rlBack;
    private TextView tvClientName;
    private TextView tvProjectName;
    private TextView tvProjectDescription;
    private RecyclerView rvEmployees;
    private TextView tvNoEmployeeAssigned;
    private TextView tvProjectAllottedHours;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_project_detail);
        initBundle ();
        initView ();
        initData ();
        initListener ();
        initAdapter ();
        setData ();
    }
    
    private void initListener () {
        fabAddEmployee.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                EmployeeListDialogFragment fragment = EmployeeListDialogFragment.newInstance ();
                fragment.setOnDialogResultListener (new EmployeeListDialogFragment.OnDialogResultListener () {
                    @Override
                    public void onPositiveResult (int emp_id, String employee_name) {
                        Utils.hideSoftKeyboard (ProjectDetailActivity.this);
                        employee_id = emp_id;
                        final MaterialDialog.Builder builder = new MaterialDialog.Builder (ProjectDetailActivity.this)
                                .customView (R.layout.dialog_assign_employee, true)
                                .positiveText (R.string.dialog_action_save)
                                .negativeText (R.string.dialog_action_cancel);
                        final MaterialDialog dialog = builder.build ();
    
                        final TextView tvEmployeeName = dialog.getCustomView ().findViewById (R.id.tvEmployeeName);
                        final TextView tvCounterDescription = dialog.getCustomView ().findViewById (R.id.tvCounterDescription);
                        final EditText etDescription = dialog.getCustomView ().findViewById (R.id.etDescription);
                        final EditText etRole = dialog.getCustomView ().findViewById (R.id.etRole);
                        
                        Utils.setTypefaceToAllViews (ProjectDetailActivity.this, tvEmployeeName);
    
                        tvEmployeeName.setText (employee_name);
    
                        etDescription.addTextChangedListener (new TextWatcher () {
                            @Override
                            public void onTextChanged (CharSequence s, int start, int before, int count) {
                                int len = s.toString ().trim ().length ();
                                tvCounterDescription.setText (len + "/80");
                                if (len > 80) {
                                    dialog.getActionButton (DialogAction.POSITIVE).setEnabled (false);
                                    tvCounterDescription.setTextColor (getResources ().getColor (R.color.md_red_900));
                                } else {
                                    dialog.getActionButton (DialogAction.POSITIVE).setEnabled (true);
                                    tvCounterDescription.setTextColor (getResources ().getColor (R.color.secondary_text));
                                }
                            }
        
                            @Override
                            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                            }
        
                            @Override
                            public void afterTextChanged (Editable s) {
                            }
                        });
    
    
                        etRole.setText (roleList.get (0).getRole_title ());
                        role_id = roleList.get (0).getRole_id ();
    
                        etRole.setOnClickListener (new View.OnClickListener () {
                            @Override
                            public void onClick (View v) {
                                new MaterialDialog.Builder (ProjectDetailActivity.this)
                                        .items (roleTitleList)
                                        .typeface (SetTypeFace.getTypeface (ProjectDetailActivity.this), SetTypeFace.getTypeface (ProjectDetailActivity.this))
                                        .itemsCallback (new MaterialDialog.ListCallback () {
                                            @Override
                                            public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                                etRole.setText (text.toString ());
                                                for (int i = 0; i < roleList.size (); i++) {
                                                    Role role = roleList.get (i);
                                                    if (role.getRole_title ().equalsIgnoreCase (text.toString ())) {
                                                        role_id = role.getRole_id ();
                                                    }
                                                }
                                                etRole.setError (null);
                                            }
                                        })
                                        .show ();
                            }
                        });
    
                        builder.onPositive (new MaterialDialog.SingleButtonCallback () {
                            @Override
                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Utils.hideSoftKeyboard (ProjectDetailActivity.this);
                                assignProject (project_id, employee_id, role_id, etDescription.getText ().toString ().trim ());
                            }
                        });
    
                        dialog.show ();
                    }
    
                    @Override
                    public void onNegativeResult () {
                    }
                });
                fragment.show (ft, AppConfigTags.EMPLOYEES);
            }
        });
    
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        tvProjectName = (TextView) findViewById (R.id.tvProjectName);
        tvClientName = (TextView) findViewById (R.id.tvClientName);
        tvProjectDescription = (TextView) findViewById (R.id.tvProjectDescription);
        tvProjectAllottedHours = (TextView) findViewById (R.id.tvProjectAllottedHours);
        rvEmployees = (RecyclerView) findViewById (R.id.rvEmployees);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        tvNoEmployeeAssigned = (TextView) findViewById (R.id.tvNoEmployeeAssigned);
        fabAddEmployee = (FloatingActionButton) findViewById (R.id.fabAddEmployee);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, clMain);
        appDetailsPref = AppDetailsPref.getInstance ();
        progressDialog = new ProgressDialog (this);
    }
    
    private void initBundle () {
        project_id = getIntent ().getExtras ().getInt (AppConfigTags.PROJECT_ID);
        projects = getIntent ().getExtras ().getString (AppConfigTags.PROJECT);
    }
    
    private void initAdapter () {
        employee2Adapter = new Employee2Adapter (ProjectDetailActivity.this, employee2List, project_id);
        rvEmployees.setAdapter (employee2Adapter);
        rvEmployees.setHasFixedSize (true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false);
        rvEmployees.setLayoutManager (linearLayoutManager);
/*
        rvEmployees.addItemDecoration (new RecyclerViewMargin (
                (int) Utils.pxFromDp (this, 4),
                (int) Utils.pxFromDp (this, 4),
                (int) Utils.pxFromDp (this, 0),
                (int) Utils.pxFromDp (this, 0),
                1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
*/
        
        rvEmployees.setItemAnimator (new DefaultItemAnimator ());
    }
    
    private void setData () {
        try {
            JSONObject jsonObject = new JSONObject (projects);
            project_id = jsonObject.getInt (AppConfigTags.PROJECT_ID);
            tvClientName.setText (jsonObject.getString (AppConfigTags.CLIENT_NAME));
            tvProjectName.setText (jsonObject.getString (AppConfigTags.PROJECT_TITLE));
            if (jsonObject.getString (AppConfigTags.PROJECT_DESCRIPTION).length () > 0) {
                tvProjectDescription.setText (jsonObject.getString (AppConfigTags.PROJECT_DESCRIPTION));
            } else {
                tvProjectDescription.setText ("No Description Available");
            }
            tvProjectAllottedHours.setText (jsonObject.getString (AppConfigTags.PROJECT_ALLOTTED_HOUR) + " Hrs");
    
            JSONArray employees = jsonObject.getJSONArray (AppConfigTags.EMPLOYEES);
            if (employees.length () == 0) {
                tvNoEmployeeAssigned.setVisibility (View.VISIBLE);
                rvEmployees.setVisibility (View.GONE);
            } else {
                tvNoEmployeeAssigned.setVisibility (View.GONE);
                rvEmployees.setVisibility (View.VISIBLE);
    
                for (int i = 0; i < employees.length (); i++) {
                    JSONObject employee = employees.getJSONObject (i);
                    employee2List.add (i, new Employee2 (
                            employee.getInt (AppConfigTags.EMPLOYEE_ID),
                            employee.getString (AppConfigTags.EMPLOYEE_NAME),
                            employee.getString (AppConfigTags.EMPLOYEE_ROLE)
                    ));
                    employee2Adapter.notifyDataSetChanged ();
                }
            }
    
            JSONArray roles = new JSONArray (appDetailsPref.getStringPref (this, AppDetailsPref.ROLES));
    
            for (int j = 0; j < roles.length (); j++) {
                JSONObject jsonObject1 = roles.getJSONObject (j);
                roleList.add (j, new Role (jsonObject1.getInt (AppConfigTags.ROLE_ID), jsonObject1.getString (AppConfigTags.ROLE_TITLE)));
                roleTitleList.add (j, jsonObject1.getString (AppConfigTags.ROLE_TITLE));
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    private void assignProject (final int project_id, final int employee_id, final int role_id, final String description) {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showProgressDialog (this, progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.ADD_PROJECT_OWNER, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.ADD_PROJECT_OWNER,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        employee2List.clear ();
                                        JSONArray employees = jsonObj.getJSONArray (AppConfigTags.EMPLOYEES);
                                        if (employees.length () == 0) {
                                            tvNoEmployeeAssigned.setVisibility (View.VISIBLE);
                                            rvEmployees.setVisibility (View.GONE);
                                        } else {
                                            tvNoEmployeeAssigned.setVisibility (View.GONE);
                                            rvEmployees.setVisibility (View.VISIBLE);
                                            
                                            for (int i = 0; i < employees.length (); i++) {
                                                JSONObject employee = employees.getJSONObject (i);
                                                employee2List.add (i, new Employee2 (
                                                        employee.getInt (AppConfigTags.EMPLOYEE_ID),
                                                        employee.getString (AppConfigTags.EMPLOYEE_NAME),
                                                        employee.getString (AppConfigTags.EMPLOYEE_ROLE)
                                                ));
                                                employee2Adapter.notifyDataSetChanged ();
                                            }
                                        }
                                        
                                        
                                    } else {
                                        Utils.showSnackBar (ProjectDetailActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (ProjectDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (ProjectDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showSnackBar (ProjectDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.PROJECT_ID, String.valueOf (project_id));
                    params.put (AppConfigTags.EMPLOYEE_ID, String.valueOf (employee_id));
                    params.put (AppConfigTags.ROLE_ID, String.valueOf (role_id));
                    params.put (AppConfigTags.DESCRIPTION, description);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (ProjectDetailActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
}
