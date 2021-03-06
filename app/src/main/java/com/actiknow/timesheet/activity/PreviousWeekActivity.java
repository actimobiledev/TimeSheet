package com.actiknow.timesheet.activity;

import android.app.FragmentTransaction;
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
import com.actiknow.timesheet.dialog.ProjectListDialogFragment;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;


public class PreviousWeekActivity extends AppCompatActivity {
    Bundle savedInstanceState;
    ProgressDialog progressDialog;
    CoordinatorLayout clMain;
    String projects_name = "";
    int project_id = 0;
    
    float day1_total = 0;
    float day2_total = 0;
    float day3_total = 0;
    float day4_total = 0;
    float day5_total = 0;
    float day6_total = 0;
    float day7_total = 0;
    
    String projects_json = "";
    
    LinearLayout llPrevious;
    AppDetailsPref appDetailsPref = AppDetailsPref.getInstance ();
    private RelativeLayout rlBack;
    private TextView tvTitle;
    private TextView tvDate1;
    private TextView tvDate1Total;
    private EditText etMondayhour;
    private TextView tvDate2;
    private TextView tvDate2Total;
    private EditText etTueshour;
    private TextView tvDate3;
    private TextView tvDate3Total;
    private EditText etWednesdayHour;
    private TextView tvDate4;
    private TextView tvDate4Total;
    private EditText etThursdayhour;
    private TextView tvDate5;
    private TextView tvDate5Total;
    private EditText etFridayhour;
    private TextView tvDate6;
    private TextView tvDate6Total;
    private EditText etSaturdayhour;
    private TextView tvDate7;
    private TextView tvDate7Total;
    private EditText etSundayhour;
    private ImageView ivSave;
    
    private TextView tvProjectName;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_previous_week);
        this.savedInstanceState = savedInstanceState;
        initView ();
        initData ();
        
        getPreviousWeekDetails ();
        initListener ();
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        tvDate1 = (TextView) findViewById (R.id.tvDate1);
        tvDate1Total = (TextView) findViewById (R.id.tvDate1Total);
        etMondayhour = (EditText) findViewById (R.id.etMondayhour);
        tvDate2 = (TextView) findViewById (R.id.tvDate2);
        tvDate2Total = (TextView) findViewById (R.id.tvDate2Total);
        etTueshour = (EditText) findViewById (R.id.etTueshour);
        tvDate3 = (TextView) findViewById (R.id.tvDate3);
        tvDate3Total = (TextView) findViewById (R.id.tvDate3Total);
        etWednesdayHour = (EditText) findViewById (R.id.etWednesdayHour);
        tvDate4 = (TextView) findViewById (R.id.tvDate4);
        tvDate4Total = (TextView) findViewById (R.id.tvDate4Total);
        etThursdayhour = (EditText) findViewById (R.id.etThursdayhour);
        tvDate5 = (TextView) findViewById (R.id.tvDate5);
        tvDate5Total = (TextView) findViewById (R.id.tvDate5Total);
        etFridayhour = (EditText) findViewById (R.id.etFridayhour);
        tvDate6 = (TextView) findViewById (R.id.tvDate6);
        tvDate6Total = (TextView) findViewById (R.id.tvDate6Total);
        etSaturdayhour = (EditText) findViewById (R.id.etSaturdayhour);
        tvDate7 = (TextView) findViewById (R.id.tvDate7);
        tvDate7Total = (TextView) findViewById (R.id.tvDate7Total);
        etSundayhour = (EditText) findViewById (R.id.etSundayhour);
        tvProjectName = (TextView) findViewById (R.id.tvProjectName);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        
        tvProjectName.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                ProjectListDialogFragment fragment = ProjectListDialogFragment.newInstance (projects_json);
                fragment.setOnDialogResultListener (new ProjectListDialogFragment.OnDialogResultListener () {
                    @Override
                    public void onPositiveResult (int prjct_id, String prjct_title) {
                        tvProjectName.setText (prjct_title);
                        project_id = prjct_id;
                        setData (project_id);
                    }
                    
                    @Override
                    public void onNegativeResult () {
                    }
                });
                fragment.show (ft, AppConfigTags.PROJECTS);
            }
        });
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, clMain);
        progressDialog = new ProgressDialog (PreviousWeekActivity.this);
        
        project_id = getIntent ().getExtras ().getInt (AppConfigTags.PROJECT_ID, 0);
        projects_name = getIntent ().getExtras ().getString (AppConfigTags.PROJECT_TITLE, "");
        
        tvProjectName.setText (projects_name);
    
        Calendar c = Calendar.getInstance (Locale.GERMAN);
        c.set (Calendar.DAY_OF_WEEK, c.MONDAY);
        c.add (Calendar.DATE, - 7);
        DateFormat df = new SimpleDateFormat ("dd/MM/yyyy", Locale.US);
        for (int i = 1; i < 8; i++) {
            switch (i) {
                case 1:
                    tvDate1.setText (df.format (c.getTime ()));
                    break;
                case 2:
                    tvDate2.setText (df.format (c.getTime ()));
                    break;
                case 3:
                    tvDate3.setText (df.format (c.getTime ()));
                    break;
                case 4:
                    tvDate4.setText (df.format (c.getTime ()));
                    break;
                case 5:
                    tvDate5.setText (df.format (c.getTime ()));
                    break;
                case 6:
                    tvDate6.setText (df.format (c.getTime ()));
                    break;
                case 7:
                    tvDate7.setText (df.format (c.getTime ()));
                    break;
            }
            c.add (Calendar.DATE, 1);
        }
    }
    
    private void getPreviousWeekDetails () {
        if (NetworkConnection.isNetworkAvailable (PreviousWeekActivity.this)) {
            Utils.showProgressDialog (PreviousWeekActivity.this, progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.PREVIOUS_WEEK, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.GET, AppConfigURL.PREVIOUS_WEEK,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.PROJECTS);
                                    projects_json = jsonArray.toString ();
    
                                    setData (project_id);
                                    
/*
                                    if (jsonArray.length () > 0) {
                                        for (int j = 0; j < jsonArray.length (); j++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (j);
                                            
                                            if (jsonObject.getInt (AppConfigTags.PROJECT_ID) == project_id) {
                                                tvProjectName.setText (jsonObject.getString (AppConfigTags.PROJECT_TITLE));
                                                
                                                JSONArray jsonArrayTotal = jsonObject.getJSONArray (AppConfigTags.TOTAL);
                                                if (jsonArrayTotal.length () > 0) {
                                                    for (int i = 0; i < jsonArrayTotal.length (); i++) {
                                                        JSONObject jsonObject1 = jsonArrayTotal.getJSONObject (i);
                                                        String Date = Utils.dateFormat (jsonObject1.getString (AppConfigTags.DATE));
                                                        
                                                        if (Date.equalsIgnoreCase (tvDate1.getText ().toString ())) {
                                                            day1_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                                            tvDate1Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                                        }
                                                        if (Date.equalsIgnoreCase (tvDate2.getText ().toString ())) {
                                                            day2_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                                            tvDate2Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                                        }
                                                        if (Date.equalsIgnoreCase (tvDate3.getText ().toString ())) {
                                                            day3_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                                            tvDate3Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                                        }
                                                        if (Date.equalsIgnoreCase (tvDate4.getText ().toString ())) {
                                                            day4_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                                            tvDate4Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                                        }
                                                        if (Date.equalsIgnoreCase (tvDate5.getText ().toString ())) {
                                                            day5_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                                            tvDate5Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                                        }
                                                        if (Date.equalsIgnoreCase (tvDate6.getText ().toString ())) {
                                                            day6_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                                            tvDate6Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                                        }
                                                        if (Date.equalsIgnoreCase (tvDate7.getText ().toString ())) {
                                                            day7_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                                            tvDate7Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                                        }
                                                    }
                                                } else {
                                                    tvDate1Total.setText ("0");
                                                    tvDate2Total.setText ("0");
                                                    tvDate3Total.setText ("0");
                                                    tvDate4Total.setText ("0");
                                                    tvDate5Total.setText ("0");
                                                    tvDate6Total.setText ("0");
                                                    tvDate7Total.setText ("0");
                                                }
                                                
                                                JSONArray jsonArrayHour = jsonObject.getJSONArray (AppConfigTags.HOURS);
                                                if (jsonArrayHour.length () > 0) {
                                                    for (int i = 0; i < jsonArrayHour.length (); i++) {
                                                        JSONObject jsonObject2 = jsonArrayHour.getJSONObject (i);
                                                        String Date = Utils.dateFormat (jsonObject2.getString (AppConfigTags.DATE));
                                                        
                                                        if (Date.equalsIgnoreCase (tvDate1.getText ().toString ())) {
                                                            etMondayhour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                                        }
                                                        if (Date.equalsIgnoreCase (tvDate2.getText ().toString ())) {
                                                            etTueshour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                                        }
                                                        if (Date.equalsIgnoreCase (tvDate3.getText ().toString ())) {
                                                            etWednesdayHour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                                        }
                                                        if (Date.equalsIgnoreCase (tvDate4.getText ().toString ())) {
                                                            etThursdayhour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                                        }
                                                        if (Date.equalsIgnoreCase (tvDate5.getText ().toString ())) {
                                                            etFridayhour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                                        }
                                                        if (Date.equalsIgnoreCase (tvDate6.getText ().toString ())) {
                                                            etSaturdayhour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                                        }
                                                        if (Date.equalsIgnoreCase (tvDate7.getText ().toString ())) {
                                                            etSundayhour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                                        }
                                                    }
                                                } else {
                                                    etMondayhour.setText ("0");
                                                    etTueshour.setText ("0");
                                                    etWednesdayHour.setText ("0");
                                                    etThursdayhour.setText ("0");
                                                    etFridayhour.setText ("0");
                                                    etSaturdayhour.setText ("0");
                                                    etSundayhour.setText ("0");
                                                }


//                                                etMondayhour.setText (jsonObject.getString (AppConfigTags.DAY_1));
//                                                etTueshour.setText (jsonObject.getString (AppConfigTags.DAY_2));
//                                                etWednesdayHour.setText (jsonObject.getString (AppConfigTags.DAY_3));
//                                                etThursdayhour.setText (jsonObject.getString (AppConfigTags.DAY_4));
//                                                etFridayhour.setText (jsonObject.getString (AppConfigTags.DAY_5));
//                                                etSaturdayhour.setText (jsonObject.getString (AppConfigTags.DAY_6));
//                                                etSundayhour.setText (jsonObject.getString (AppConfigTags.DAY_7));
                                            } else {
                                                etMondayhour.setText ("0");
                                                etTueshour.setText ("0");
                                                etWednesdayHour.setText ("0");
                                                etThursdayhour.setText ("0");
                                                etFridayhour.setText ("0");
                                                etSaturdayhour.setText ("0");
                                                etSundayhour.setText ("0");
                                            }
                                        }
                                    }
*/
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (PreviousWeekActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else
                            
                            {
                                Utils.showSnackBar (PreviousWeekActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (PreviousWeekActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
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
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (PreviousWeekActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else
        
        {
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
    
    private void setData (int project_id) {
        try {
            JSONArray jsonArray = new JSONArray (projects_json);
            if (jsonArray.length () > 0) {
                for (int j = 0; j < jsonArray.length (); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject (j);
                    
                    if (jsonObject.getInt (AppConfigTags.PROJECT_ID) == project_id) {
                        tvProjectName.setText (jsonObject.getString (AppConfigTags.PROJECT_TITLE));
                        
                        JSONArray jsonArrayTotal = jsonObject.getJSONArray (AppConfigTags.TOTAL);
                        if (jsonArrayTotal.length () > 0) {
                            for (int i = 0; i < jsonArrayTotal.length (); i++) {
                                JSONObject jsonObject1 = jsonArrayTotal.getJSONObject (i);
                                String Date = Utils.dateFormat (jsonObject1.getString (AppConfigTags.DATE));
                                
                                if (Date.equalsIgnoreCase (tvDate1.getText ().toString ())) {
                                    day1_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                    tvDate1Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                }
                                if (Date.equalsIgnoreCase (tvDate2.getText ().toString ())) {
                                    day2_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                    tvDate2Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                }
                                if (Date.equalsIgnoreCase (tvDate3.getText ().toString ())) {
                                    day3_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                    tvDate3Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                }
                                if (Date.equalsIgnoreCase (tvDate4.getText ().toString ())) {
                                    day4_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                    tvDate4Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                }
                                if (Date.equalsIgnoreCase (tvDate5.getText ().toString ())) {
                                    day5_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                    tvDate5Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                }
                                if (Date.equalsIgnoreCase (tvDate6.getText ().toString ())) {
                                    day6_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                    tvDate6Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                }
                                if (Date.equalsIgnoreCase (tvDate7.getText ().toString ())) {
                                    day7_total = (float) jsonObject1.getDouble (AppConfigTags.HOUR);
                                    tvDate7Total.setText (jsonObject1.getString (AppConfigTags.HOUR));
                                }
                            }
                        } else {
                            tvDate1Total.setText ("0");
                            tvDate2Total.setText ("0");
                            tvDate3Total.setText ("0");
                            tvDate4Total.setText ("0");
                            tvDate5Total.setText ("0");
                            tvDate6Total.setText ("0");
                            tvDate7Total.setText ("0");
                        }
                        
                        JSONArray jsonArrayHour = jsonObject.getJSONArray (AppConfigTags.HOURS);
                        if (jsonArrayHour.length () > 0) {
                            for (int i = 0; i < jsonArrayHour.length (); i++) {
                                JSONObject jsonObject2 = jsonArrayHour.getJSONObject (i);
                                String Date = Utils.dateFormat (jsonObject2.getString (AppConfigTags.DATE));
                                Log.e ("karman", "date : " + Date);
                                if (Date.equalsIgnoreCase (tvDate1.getText ().toString ())) {
                                    etMondayhour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                }
                                if (Date.equalsIgnoreCase (tvDate2.getText ().toString ())) {
                                    etTueshour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                }
                                if (Date.equalsIgnoreCase (tvDate3.getText ().toString ())) {
                                    etWednesdayHour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                }
                                if (Date.equalsIgnoreCase (tvDate4.getText ().toString ())) {
                                    etThursdayhour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                }
                                if (Date.equalsIgnoreCase (tvDate5.getText ().toString ())) {
                                    etFridayhour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                }
                                if (Date.equalsIgnoreCase (tvDate6.getText ().toString ())) {
                                    etSaturdayhour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                }
                                if (Date.equalsIgnoreCase (tvDate7.getText ().toString ())) {
                                    etSundayhour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                }
                            }
                        } else {
                            etMondayhour.setText ("0");
                            etTueshour.setText ("0");
                            etWednesdayHour.setText ("0");
                            etThursdayhour.setText ("0");
                            etFridayhour.setText ("0");
                            etSaturdayhour.setText ("0");
                            etSundayhour.setText ("0");
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
}