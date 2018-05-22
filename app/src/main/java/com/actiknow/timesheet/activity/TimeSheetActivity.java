package com.actiknow.timesheet.activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.dialog.ProjectListDialogFragment;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;


public class TimeSheetActivity extends AppCompatActivity {
    Bundle savedInstanceState;
    ProgressDialog progressDialog;
    CoordinatorLayout clMain;
    String projects_json = "";
    int project_id = 0;
    
    String day1 = "0";
    String day2 = "0";
    String day3 = "0";
    String day4 = "0";
    String day5 = "0";
    String day6 = "0";
    String day7 = "0";
    
    float day1_total = 0;
    float day2_total = 0;
    float day3_total = 0;
    float day4_total = 0;
    float day5_total = 0;
    float day6_total = 0;
    float day7_total = 0;
    
    String day1_comment = "";
    String day2_comment = "";
    String day3_comment = "";
    String day4_comment = "";
    String day5_comment = "";
    String day6_comment = "";
    String day7_comment = "";
    
    
    RelativeLayout rlPrevious;
    AppDetailsPref appDetailsPref = AppDetailsPref.getInstance ();
    private RelativeLayout rlBack;
    private TextView tvTitle;
    private TextView tvDate1;
    private TextView tvDate1Total;
    private EditText etMondayHour;
    private ImageView ivMondayComment;
    private TextView tvDate2;
    private TextView tvDate2Total;
    private EditText etTuesdayHour;
    private ImageView ivTuesdayComment;
    private TextView tvDate3;
    private TextView tvDate3Total;
    private EditText etWednesdayHour;
    private ImageView ivWednesdayComment;
    private TextView tvDate4;
    private TextView tvDate4Total;
    private EditText etThursdayHour;
    private ImageView ivThursdayComment;
    private TextView tvDate5;
    private TextView tvDate5Total;
    private EditText etFridayHour;
    private ImageView ivFridayComment;
    private TextView tvDate6;
    private TextView tvDate6Total;
    private EditText etSaturdayHour;
    private ImageView ivSaturdayComment;
    private TextView tvDate7;
    private TextView tvDate7Total;
    private EditText etSundayHour;
    private ImageView ivSundayComment;
    private ImageView ivSave;
    
    private TextView tvProjectName;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_time_sheet);
        this.savedInstanceState = savedInstanceState;
        initView ();
        initData ();
        setData (project_id);
        initListener ();
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        tvDate1 = (TextView) findViewById (R.id.tvDate1);
        tvDate1Total = (TextView) findViewById (R.id.tvDate1Total);
        etMondayHour = (EditText) findViewById (R.id.etMondayhour);
        ivMondayComment = (ImageView) findViewById (R.id.ivMondayComment);
        tvDate2 = (TextView) findViewById (R.id.tvDate2);
        tvDate2Total = (TextView) findViewById (R.id.tvDate2Total);
        etTuesdayHour = (EditText) findViewById (R.id.etTueshour);
        ivTuesdayComment = (ImageView) findViewById (R.id.ivTuesdayComment);
        tvDate3 = (TextView) findViewById (R.id.tvDate3);
        tvDate3Total = (TextView) findViewById (R.id.tvDate3Total);
        etWednesdayHour = (EditText) findViewById (R.id.etWednesdayHour);
        ivWednesdayComment = (ImageView) findViewById (R.id.ivWednesdayComment);
        tvDate4 = (TextView) findViewById (R.id.tvDate4);
        tvDate4Total = (TextView) findViewById (R.id.tvDate4Total);
        etThursdayHour = (EditText) findViewById (R.id.etThursdayhour);
        ivThursdayComment = (ImageView) findViewById (R.id.ivThursdayComment);
        tvDate5 = (TextView) findViewById (R.id.tvDate5);
        tvDate5Total = (TextView) findViewById (R.id.tvDate5Total);
        etFridayHour = (EditText) findViewById (R.id.etFridayhour);
        ivFridayComment = (ImageView) findViewById (R.id.ivFridayComment);
        tvDate6 = (TextView) findViewById (R.id.tvDate6);
        tvDate6Total = (TextView) findViewById (R.id.tvDate6Total);
        etSaturdayHour = (EditText) findViewById (R.id.etSaturdayhour);
        ivSaturdayComment = (ImageView) findViewById (R.id.ivSaturdayComment);
        tvDate7 = (TextView) findViewById (R.id.tvDate7);
        tvDate7Total = (TextView) findViewById (R.id.tvDate7Total);
        etSundayHour = (EditText) findViewById (R.id.etSundayhour);
        ivSundayComment = (ImageView) findViewById (R.id.ivSundayComment);
        ivSave = (ImageView) findViewById (R.id.ivSave);
        tvProjectName = (TextView) findViewById (R.id.tvProjectName);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        rlPrevious = (RelativeLayout) findViewById (R.id.llPrevious);
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
//                sendProjectDetailsToServer (true);
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
                        sendProjectDetailsToServer (false);
                        project_id = prjct_id;
                    }
    
                    @Override
                    public void onNegativeResult () {
                    }
                });
                fragment.show (ft, AppConfigTags.PROJECTS);
            }
        });
        
        ivSave.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                sendProjectDetailsToServer (true);
            }
        });
    
        rlPrevious.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent (TimeSheetActivity.this, PreviousWeekActivity.class);
                intent.putExtra (AppConfigTags.PROJECT_ID, project_id);
                intent.putExtra (AppConfigTags.PROJECT_TITLE, tvProjectName.getText ().toString ());
                startActivity (intent);
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        
/*
        etMondayhour.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (s.toString ().trim ().length () > 0) {
                    float tmp = Float.parseFloat (s.toString ().trim ());
                    tvDate1Total.setText (String.valueOf (day1_total + tmp));
                } else{
                    tvDate1Total.setText (String.valueOf (day1_total));
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                
                // TODO Auto-generated method stub
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
*/
    
        ivMondayComment.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                showInputDialog (1);
            }
        });
        ivTuesdayComment.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                showInputDialog (2);
            }
        });
        ivWednesdayComment.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                showInputDialog (3);
            }
        });
        ivThursdayComment.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                showInputDialog (4);
            }
        });
        ivFridayComment.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                showInputDialog (5);
            }
        });
        ivSaturdayComment.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                showInputDialog (6);
            }
        });
        ivSundayComment.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                showInputDialog (7);
            }
        });
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, clMain);
        progressDialog = new ProgressDialog (TimeSheetActivity.this);
        
        project_id = getIntent ().getExtras ().getInt (AppConfigTags.PROJECT_ID, 0);
        projects_json = getIntent ().getExtras ().getString (AppConfigTags.PROJECTS, "");
    
        Calendar c = Calendar.getInstance (Locale.GERMAN);
        c.set (Calendar.DAY_OF_WEEK, c.MONDAY);
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
                                    etMondayHour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                    day1_comment = jsonObject2.getString (AppConfigTags.DESCRIPTION);
                                    if (day1_comment.length () > 0) {
                                        ivMondayComment.setImageResource (R.drawable.ic_comment_1);
                                    } else {
                                        ivMondayComment.setImageResource (R.drawable.ic_comment_2);
                                    }
                                }
                                if (Date.equalsIgnoreCase (tvDate2.getText ().toString ())) {
                                    etTuesdayHour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                    day2_comment = jsonObject2.getString (AppConfigTags.DESCRIPTION);
                                    if (day2_comment.length () > 0) {
                                        ivTuesdayComment.setImageResource (R.drawable.ic_comment_1);
                                    } else {
                                        ivTuesdayComment.setImageResource (R.drawable.ic_comment_2);
                                    }
                                }
                                if (Date.equalsIgnoreCase (tvDate3.getText ().toString ())) {
                                    etWednesdayHour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                    day3_comment = jsonObject2.getString (AppConfigTags.DESCRIPTION);
                                    if (day3_comment.length () > 0) {
                                        ivWednesdayComment.setImageResource (R.drawable.ic_comment_1);
                                    } else {
                                        ivWednesdayComment.setImageResource (R.drawable.ic_comment_2);
                                    }
                                }
                                if (Date.equalsIgnoreCase (tvDate4.getText ().toString ())) {
                                    etThursdayHour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                    day4_comment = jsonObject2.getString (AppConfigTags.DESCRIPTION);
                                    if (day4_comment.length () > 0) {
                                        ivThursdayComment.setImageResource (R.drawable.ic_comment_1);
                                    } else {
                                        ivThursdayComment.setImageResource (R.drawable.ic_comment_2);
                                    }
                                }
                                if (Date.equalsIgnoreCase (tvDate5.getText ().toString ())) {
                                    etFridayHour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                    day5_comment = jsonObject2.getString (AppConfigTags.DESCRIPTION);
                                    if (day5_comment.length () > 0) {
                                        ivFridayComment.setImageResource (R.drawable.ic_comment_1);
                                    } else {
                                        ivFridayComment.setImageResource (R.drawable.ic_comment_2);
                                    }
                                }
                                if (Date.equalsIgnoreCase (tvDate6.getText ().toString ())) {
                                    etSaturdayHour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                    day6_comment = jsonObject2.getString (AppConfigTags.DESCRIPTION);
                                    if (day6_comment.length () > 0) {
                                        ivSaturdayComment.setImageResource (R.drawable.ic_comment_1);
                                    } else {
                                        ivSaturdayComment.setImageResource (R.drawable.ic_comment_2);
                                    }
                                }
                                if (Date.equalsIgnoreCase (tvDate7.getText ().toString ())) {
                                    etSundayHour.setText (jsonObject2.getString (AppConfigTags.HOUR));
                                    day7_comment = jsonObject2.getString (AppConfigTags.DESCRIPTION);
                                    if (day7_comment.length () > 0) {
                                        ivSundayComment.setImageResource (R.drawable.ic_comment_1);
                                    } else {
                                        ivSundayComment.setImageResource (R.drawable.ic_comment_2);
                                    }
                                }
                            }
                        } else {
                            etMondayHour.setText ("0");
                            etTuesdayHour.setText ("0");
                            etWednesdayHour.setText ("0");
                            etThursdayHour.setText ("0");
                            etFridayHour.setText ("0");
                            etSaturdayHour.setText ("0");
                            etSundayHour.setText ("0");
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
//        sendProjectDetailsToServer (true);
    }
    
    private void sendProjectDetailsToServer (final boolean finish) {
        JSONObject jsonObject = new JSONObject ();
        String timeSheet = "";
        try {
            DateFormat inputFormat = new SimpleDateFormat ("dd/MM/yyyy");
            DateFormat outputFormat = new SimpleDateFormat ("yyyy-MM-dd");
            String inputDateStr = tvDate1.getText ().toString ();
            String inputDateStr2 = tvDate7.getText ().toString ();
            Date date = null;
            Date date1 = null;
            date = inputFormat.parse (inputDateStr);
            date1 = inputFormat.parse (inputDateStr2);
            String startDate = outputFormat.format (date);
            String endDate = outputFormat.format (date1);
    
    
            day1 = etMondayHour.getText ().toString ().trim ();
            day2 = etTuesdayHour.getText ().toString ().trim ();
            day3 = etWednesdayHour.getText ().toString ().trim ();
            day4 = etThursdayHour.getText ().toString ().trim ();
            day5 = etFridayHour.getText ().toString ().trim ();
            day6 = etSaturdayHour.getText ().toString ().trim ();
            day7 = etSundayHour.getText ().toString ().trim ();
            
            
            jsonObject.put ("project_id", project_id);
            jsonObject.put ("start_date", startDate);
            jsonObject.put ("end_date", endDate);
            jsonObject.put ("day_1", day1.equalsIgnoreCase ("") ? "0" : etMondayHour.getText ().toString ());
            jsonObject.put ("day_1_comment", day1_comment);
            jsonObject.put ("day_2", day2.equalsIgnoreCase ("") ? "0" : etTuesdayHour.getText ().toString ());
            jsonObject.put ("day_2_comment", day2_comment);
            jsonObject.put ("day_3", day3.equalsIgnoreCase ("") ? "0" : etWednesdayHour.getText ().toString ());
            jsonObject.put ("day_3_comment", day3_comment);
            jsonObject.put ("day_4", day4.equalsIgnoreCase ("") ? "0" : etThursdayHour.getText ().toString ());
            jsonObject.put ("day_4_comment", day4_comment);
            jsonObject.put ("day_5", day5.equalsIgnoreCase ("") ? "0" : etFridayHour.getText ().toString ());
            jsonObject.put ("day_5_comment", day5_comment);
            jsonObject.put ("day_6", day6.equalsIgnoreCase ("") ? "0" : etSaturdayHour.getText ().toString ());
            jsonObject.put ("day_6_comment", day6_comment);
            jsonObject.put ("day_7", day7.equalsIgnoreCase ("") ? "0" : etSundayHour.getText ().toString ());
            jsonObject.put ("day_7_comment", day7_comment);
    
            timeSheet = jsonObject.toString ();
    
        } catch (Exception e) {
            e.printStackTrace ();
        }
        
        
        if (NetworkConnection.isNetworkAvailable (TimeSheetActivity.this)) {
            Utils.showProgressDialog (TimeSheetActivity.this, progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.ADD_TASK, true);
            final String finalTimeSheet = timeSheet;
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.ADD_TASK,
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
                                        projects_json = jsonObj.getJSONArray (AppConfigTags.PROJECTS).toString ();
                                        setData (project_id);
                                        if (finish) {
                                            finish ();
                                            overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                                        }
                                    } else {
                                        Utils.showSnackBar (TimeSheetActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (TimeSheetActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (TimeSheetActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (TimeSheetActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.TIMESHEET, finalTimeSheet);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (TimeSheetActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
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
    
    public void showInputDialog (final int day) {
        final MaterialDialog.Builder mBuilder = new MaterialDialog.Builder (this)
                .content ("Enter Description")
                .contentColor (getResources ().getColor (R.color.primary_text))
                .positiveColor (getResources ().getColor (R.color.primary_text))
                .typeface (SetTypeFace.getTypeface (this), SetTypeFace.getTypeface (this))
                .inputRangeRes (0, 255, R.color.text_color_red)
                .alwaysCallInputCallback ()
                .canceledOnTouchOutside (true)
                .cancelable (true)
                .positiveText (R.string.dialog_action_submit);
        
        
        mBuilder.input (getResources ().getString (R.string.dialog_hint_optional), null, new MaterialDialog.InputCallback () {
            @Override
            public void onInput (MaterialDialog dialog, CharSequence input) {
                if (input.toString ().length () == 0) {
                    mBuilder.positiveText (R.string.dialog_action_submit);
                } else {
                    mBuilder.positiveText (R.string.dialog_action_submit);
                }
            }
        });
        
        mBuilder.onPositive (new MaterialDialog.SingleButtonCallback () {
            @Override
            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                switch (day) {
                    case 1:
                        day1_comment = dialog.getInputEditText ().getText ().toString ();
                        if (day1_comment.length () > 0) {
                            ivMondayComment.setImageResource (R.drawable.ic_comment_1);
                        } else {
                            ivMondayComment.setImageResource (R.drawable.ic_comment_2);
                        }
                        break;
                    case 2:
                        day2_comment = dialog.getInputEditText ().getText ().toString ();
                        if (day2_comment.length () > 0) {
                            ivTuesdayComment.setImageResource (R.drawable.ic_comment_1);
                        } else {
                            ivTuesdayComment.setImageResource (R.drawable.ic_comment_2);
                        }
                        break;
                    case 3:
                        day3_comment = dialog.getInputEditText ().getText ().toString ();
                        if (day3_comment.length () > 0) {
                            ivWednesdayComment.setImageResource (R.drawable.ic_comment_1);
                        } else {
                            ivWednesdayComment.setImageResource (R.drawable.ic_comment_2);
                        }
                        break;
                    case 4:
                        day4_comment = dialog.getInputEditText ().getText ().toString ();
                        if (day4_comment.length () > 0) {
                            ivThursdayComment.setImageResource (R.drawable.ic_comment_1);
                        } else {
                            ivThursdayComment.setImageResource (R.drawable.ic_comment_2);
                        }
                        break;
                    case 5:
                        day5_comment = dialog.getInputEditText ().getText ().toString ();
                        if (day5_comment.length () > 0) {
                            ivFridayComment.setImageResource (R.drawable.ic_comment_1);
                        } else {
                            ivFridayComment.setImageResource (R.drawable.ic_comment_2);
                        }
                        break;
                    case 6:
                        day6_comment = dialog.getInputEditText ().getText ().toString ();
                        if (day6_comment.length () > 0) {
                            ivSaturdayComment.setImageResource (R.drawable.ic_comment_1);
                        } else {
                            ivSaturdayComment.setImageResource (R.drawable.ic_comment_2);
                        }
                        break;
                    case 7:
                        day7_comment = dialog.getInputEditText ().getText ().toString ();
                        if (day7_comment.length () > 0) {
                            ivSundayComment.setImageResource (R.drawable.ic_comment_1);
                        } else {
                            ivSundayComment.setImageResource (R.drawable.ic_comment_2);
                        }
                        break;
                }
            }
        });
        
        MaterialDialog dialog = mBuilder.build ();
        
        switch (day) {
            case 1:
                dialog.getInputEditText ().setText (day1_comment);
                break;
            case 2:
                dialog.getInputEditText ().setText (day2_comment);
                break;
            case 3:
                dialog.getInputEditText ().setText (day3_comment);
                break;
            case 4:
                dialog.getInputEditText ().setText (day4_comment);
                break;
            case 5:
                dialog.getInputEditText ().setText (day5_comment);
                break;
            case 6:
                dialog.getInputEditText ().setText (day6_comment);
                break;
            case 7:
                dialog.getInputEditText ().setText (day7_comment);
                break;
        }
        dialog.show ();
    }
}