package com.actiknow.timesheet.activity;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.dialog.ClientListDialogFragment;
import com.actiknow.timesheet.dialog.ClientProjectDialogFragment;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppConfigURL;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Constants;
import com.actiknow.timesheet.utils.NetworkConnection;
import com.actiknow.timesheet.utils.TypefaceSpan;
import com.actiknow.timesheet.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class AddProjectActivity extends AppCompatActivity {
    public static String client_name = "";
    ImageView ivCancel;
    EditText etStartDate;
    EditText etEndDate;
    EditText etClientName;
    EditText etProjectName;
    EditText etProjectAllottedHours;
    EditText etProjectDescription;
    TextView tvCounterName;
    TextView tvCounterDescription;
    ImageView ivProjectList;
    String start_date = "";
    String end_date = "";
    int client_id = 0;
    ArrayList<String> clientList = new ArrayList<> ();
    ArrayList<Integer> clientID = new ArrayList<> ();
    ProgressDialog progressDialog;
    CoordinatorLayout clMain;
    AppDetailsPref appDetailsPref;
    ImageView ivSave;
    RelativeLayout rlBack;
    private int mYear, mMonth, mDay;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add_project);
        initView ();
        initData ();
        setData ();
        initListener ();
    }
    
    private void initView () {
        ivCancel = (ImageView) findViewById (R.id.ivCancel);
        etStartDate = (EditText) findViewById (R.id.etStartDate);
        etEndDate = (EditText) findViewById (R.id.etEndDate);
        etClientName = (EditText) findViewById (R.id.etClientName);
        etProjectName = (EditText) findViewById (R.id.etProjectName);
        etProjectAllottedHours = (EditText) findViewById (R.id.etProjectAllottedHours);
        etProjectDescription = (EditText) findViewById (R.id.etProjectDescription);
        
        tvCounterName = (TextView) findViewById (R.id.tvCounterName);
        tvCounterDescription = (TextView) findViewById (R.id.tvCounterDescription);
        
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        ivSave = (ImageView) findViewById (R.id.ivSave);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        ivProjectList = (ImageView) findViewById (R.id.ivProjectList);
    }
    
    private void initData () {
        progressDialog = new ProgressDialog (AddProjectActivity.this);
        appDetailsPref = AppDetailsPref.getInstance ();
        Utils.setTypefaceToAllViews (this, clMain);
        
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        
        etClientName.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                ClientListDialogFragment fragment = ClientListDialogFragment.newInstance ();
                fragment.setOnDialogResultListener (new ClientListDialogFragment.OnDialogResultListener () {
                    @Override
                    public void onPositiveResult (int clnt_id, String clnt_name) {
                        client_id = clnt_id;
                        etClientName.setText (clnt_name);
                        ivProjectList.setVisibility (View.VISIBLE);
                    }
        
                    @Override
                    public void onNegativeResult () {
                    }
                });
                fragment.show (ft, AppConfigTags.EMPLOYEES);
            }
        });
        
        etStartDate.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                selectDate (etStartDate, 1);
            }
        });
        
        etEndDate.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                selectDate (etEndDate, 2);
            }
        });
        
        ivSave.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                SpannableString s1 = new SpannableString (getResources ().getString (R.string.please_select_client));
                s1.setSpan (new TypefaceSpan (AddProjectActivity.this, Constants.font_name), 0, s1.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.please_enter_project_name));
                s2.setSpan (new TypefaceSpan (AddProjectActivity.this, Constants.font_name), 0, s1.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    
                int len = etClientName.getText ().toString ().trim ().length ();
                int len2 = etProjectName.getText ().toString ().trim ().length ();
                int len3 = etProjectDescription.getText ().toString ().trim ().length ();
    
                if (len == 0) {
                    etClientName.setError (s1);
                }
                if (len2 == 0) {
                    etProjectName.setError (s2);
                }
                if (len > 0 && len2 > 0 && len2 <= 80 && len3 <= 256) {
                    sendProjectDetailsToServer (etProjectName.getText ().toString (), client_id, etProjectDescription.getText ().toString (), etProjectAllottedHours.getText ().toString ().trim ());
                }
            }
        });
        
        etProjectName.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                int len = s.toString ().trim ().length ();
                tvCounterName.setText (len + "/80");
                if (len > 80) {
                    tvCounterName.setTextColor (getResources ().getColor (R.color.md_red_900));
                } else {
                    tvCounterName.setTextColor (getResources ().getColor (R.color.secondary_text));
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        
        etProjectDescription.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                int len = s.toString ().trim ().length ();
                tvCounterDescription.setText (len + "/256");
                if (len > 256) {
                    tvCounterDescription.setTextColor (getResources ().getColor (R.color.md_red_900));
                } else {
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
    
        ivProjectList.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                ClientProjectDialogFragment fragment = ClientProjectDialogFragment.newInstance (client_id);
                fragment.setOnDialogResultListener (new ClientProjectDialogFragment.OnDialogResultListener () {
                    @Override
                    public void onPositiveResult () {
                    }
                
                    @Override
                    public void onNegativeResult () {
                    }
                });
                fragment.show (ft, AppConfigTags.PROJECTS);
            }
        });
    }
    
    private void selectDate (final EditText etPickupDate, final int i) {
        final Calendar c = Calendar.getInstance ();
        mYear = c.get (Calendar.YEAR);
        mMonth = c.get (Calendar.MONTH);
        mDay = c.get (Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog (AddProjectActivity.this, new DatePickerDialog.OnDateSetListener () {
            @Override
            public void onDateSet (DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (i == 1) {
                    start_date = year + "-" + String.format ("%02d", monthOfYear + 1) + "-" + String.format ("%02d", dayOfMonth);
                } else if (i == 2) {
                    end_date = year + "-" + String.format ("%02d", monthOfYear + 1) + "-" + String.format ("%02d", dayOfMonth);
                }
                etPickupDate.setText (String.format ("%02d", dayOfMonth) + "-" + String.format ("%02d", monthOfYear + 1) + "-" + year);
                
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show ();
    }
    
    private void setData () {
        clientID.clear ();
        clientList.clear ();
        try {
            JSONArray jsonArray = new JSONArray (appDetailsPref.getStringPref (AddProjectActivity.this, AppDetailsPref.CLIENTS));
            if (jsonArray.length () > 0) {
                for (int j = 0; j < jsonArray.length (); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject (j);
            
                    if (! AddProjectActivity.client_name.equalsIgnoreCase ("")) {
                        if (AddProjectActivity.client_name.equalsIgnoreCase (jsonObject.getString (AppConfigTags.CLIENT_NAME))) {
                            client_id = jsonObject.getInt (AppConfigTags.CLIENT_ID);
                        }
                    }
                    clientList.add (jsonObject.getString (AppConfigTags.CLIENT_NAME));
                    clientID.add (jsonObject.getInt (AppConfigTags.CLIENT_ID));
                }
            }
    
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
    
    private void sendProjectDetailsToServer (final String projectName, final int clientId, final String projectDescription, final String projectHours) {
        if (NetworkConnection.isNetworkAvailable (AddProjectActivity.this)) {
            Utils.showProgressDialog (AddProjectActivity.this, progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.ADD_PROJECT, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.ADD_PROJECT,
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
                                        finish ();
                                        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
    
                                    } else {
                                        Utils.showSnackBar (AddProjectActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (AddProjectActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (AddProjectActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (AddProjectActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
    
                    params.put (AppConfigTags.PROJECT_CLIENT_ID, String.valueOf (clientId));
                    params.put (AppConfigTags.PROJECT_TITLE, projectName);
                    params.put (AppConfigTags.PROJECT_DESCRIPTION, projectDescription);
                    params.put (AppConfigTags.PROJECT_ALLOTTED_HOUR, projectHours);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
    
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (AddProjectActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (AddProjectActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    public interface MyDialogCloseListener {
        public void handleDialogClose (DialogInterface dialog);
    }
}