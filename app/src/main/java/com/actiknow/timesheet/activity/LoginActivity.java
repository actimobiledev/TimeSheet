package com.actiknow.timesheet.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.utils.Constants;
import com.actiknow.timesheet.utils.SetTypeFace;
import com.actiknow.timesheet.utils.TypefaceSpan;

public class LoginActivity extends AppCompatActivity {
    EditText etUserName;
    EditText etPassword;
    EditText etProductCode;
    TextView tvStartSurvey;
    TextView tvShowHide;
    
    ProgressDialog progressDialog;
    CoordinatorLayout clMain;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        initView ();
        initData ();
        initListener ();
    }
    
    private void initData () {
        /*appDetailsPref = AppDetailsPref.getInstance ();
        progressDialog = new ProgressDialog (this);
        Utils.setTypefaceToAllViews (this, tvStartSurvey);
        etUserName.setText (appDetailsPref.getStringPref (this, AppDetailsPref.USER_LOGIN_ID));
        etPassword.setText (appDetailsPref.getStringPref (this, AppDetailsPref.USER_LOGIN_PASS));*/
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        tvStartSurvey = (TextView) findViewById (R.id.tvStartSurvey);
        etUserName = (EditText) findViewById (R.id.etUserName);
        etPassword = (EditText) findViewById (R.id.etPassword);
        tvShowHide = (TextView) findViewById (R.id.tvShowHide);
    }
    
    private void initListener () {
        tvStartSurvey.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                SpannableString s1 = new SpannableString (getResources ().getString (R.string.please_enter_serial_number));
                s1.setSpan (new TypefaceSpan (LoginActivity.this, Constants.font_name), 0, s1.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.please_enter_pin));
                s2.setSpan (new TypefaceSpan (LoginActivity.this, Constants.font_name), 0, s2.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                
                if (etUserName.getText ().toString ().length () == 0) {
                    etUserName.setError (s1);
                }
                if (etPassword.getText ().toString ().length () == 0) {
                    etPassword.setError (s2);
                }
    
                if ((etUserName.getText ().toString ().length () != 0) &&
                        (etPassword.getText ().toString ().length () != 0)) {
                    //sendLoginDetailsToServer (etUserName.getText ().toString (), etPassword.getText ().toString (), etProductCode.getText ().toString ());
                }
            }
        });
    
        tvShowHide.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (tvShowHide.getText ().toString ().equalsIgnoreCase ("SHOW")) {
                    tvShowHide.setText ("HIDE");
                    etPassword.setInputType (InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPassword.setSelection (etPassword.getText ().length ());
                    etPassword.setTypeface (SetTypeFace.getTypeface (LoginActivity.this));
                } else {
                    etPassword.setInputType (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword.setSelection (etPassword.getText ().length ());
                    etPassword.setTypeface (SetTypeFace.getTypeface (LoginActivity.this));
                    tvShowHide.setText ("SHOW");
                }
            }
        });
    
        etUserName.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etUserName.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        etPassword.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etPassword.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
     
    }
    
    /*private void sendLoginDetailsToServer (final String user_name, final String password, final String product_code) {
        if (NetworkConnection.isNetworkAvailable (LoginActivity.this)) {
            Utils.showProgressDialog (LoginActivity.this, progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.LOGIN, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.LOGIN,
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
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.USER_NAME, jsonObj.getString (AppConfigTags.USER_NAME));
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.USER_MOBILE, jsonObj.getString (AppConfigTags.USER_MOBILE));
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.USER_LOGIN_KEY, jsonObj.getString (AppConfigTags.USER_LOGIN_KEY));
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.USER_LOGIN_ID, jsonObj.getString (AppConfigTags.USER_LOGIN_ID));
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.USER_LOGIN_PASS, jsonObj.getString (AppConfigTags.USER_LOGIN_PASS));
                                        appDetailsPref.putIntPref (LoginActivity.this, AppDetailsPref.SURVEY_ID, jsonObj.getInt (AppConfigTags.SURVEY_ID));
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.SURVEY_NUMBER, jsonObj.getString (AppConfigTags.SURVEY_NUMBER));
                                        appDetailsPref.putIntPref (LoginActivity.this, AppDetailsPref.SURVEY_STATUS, jsonObj.getInt (AppConfigTags.SURVEY_STATUS));
                                        appDetailsPref.putIntPref (LoginActivity.this, AppDetailsPref.SURVEY_DAY_ELAPSED, jsonObj.getInt (AppConfigTags.SURVEY_DAY_ELAPSED));
                                        appDetailsPref.putIntPref (LoginActivity.this, AppDetailsPref.PRODUCT_ID, jsonObj.getInt (AppConfigTags.PRODUCT_ID));
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.PRODUCT_CODE, jsonObj.getString (AppConfigTags.PRODUCT_CODE));
    
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.WEEK_1, jsonObj.getString (AppConfigTags.WEEK_1));
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.WEEK_2, jsonObj.getString (AppConfigTags.WEEK_2));
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.WEEK_3, jsonObj.getString (AppConfigTags.WEEK_3));
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.WEEK_4, jsonObj.getString (AppConfigTags.WEEK_4));
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.WEEK_5, jsonObj.getString (AppConfigTags.WEEK_5));
                                        appDetailsPref.putStringPref (LoginActivity.this, AppDetailsPref.WEEK_6, jsonObj.getString (AppConfigTags.WEEK_6));
    
                                        appDetailsPref.putIntPref (LoginActivity.this, AppDetailsPref.WEEK_NUMBER, 1);
    
                                        if (jsonObj.getString (AppConfigTags.PRODUCT_CODE).equalsIgnoreCase (jsonObj.getString (AppConfigTags.WEEK_1))) {
                                            appDetailsPref.putIntPref (LoginActivity.this, AppDetailsPref.WEEK_NUMBER, 1);
                                        }
                                        if (jsonObj.getString (AppConfigTags.PRODUCT_CODE).equalsIgnoreCase (jsonObj.getString (AppConfigTags.WEEK_2))) {
                                            appDetailsPref.putIntPref (LoginActivity.this, AppDetailsPref.WEEK_NUMBER, 2);
                                        }
                                        if (jsonObj.getString (AppConfigTags.PRODUCT_CODE).equalsIgnoreCase (jsonObj.getString (AppConfigTags.WEEK_3))) {
                                            appDetailsPref.putIntPref (LoginActivity.this, AppDetailsPref.WEEK_NUMBER, 3);
                                        }
                                        if (jsonObj.getString (AppConfigTags.PRODUCT_CODE).equalsIgnoreCase (jsonObj.getString (AppConfigTags.WEEK_4))) {
                                            appDetailsPref.putIntPref (LoginActivity.this, AppDetailsPref.WEEK_NUMBER, 4);
                                        }
                                        if (jsonObj.getString (AppConfigTags.PRODUCT_CODE).equalsIgnoreCase (jsonObj.getString (AppConfigTags.WEEK_5))) {
                                            appDetailsPref.putIntPref (LoginActivity.this, AppDetailsPref.WEEK_NUMBER, 5);
                                        }
                                        if (jsonObj.getString (AppConfigTags.PRODUCT_CODE).equalsIgnoreCase (jsonObj.getString (AppConfigTags.WEEK_6))) {
                                            appDetailsPref.putIntPref (LoginActivity.this, AppDetailsPref.WEEK_NUMBER, 6);
                                        }
    
                                        Intent intent = new Intent (LoginActivity.this, MainActivity.class);
                                        startActivity (intent);
                                        finish ();
                                        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Utils.showSnackBar (LoginActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (LoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (LoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (LoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.LOGIN_ID, user_name);
                    params.put (AppConfigTags.LOGIN_PASSWORD, password);
                    params.put (AppConfigTags.PRODUCT_CODE, product_code);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
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
    }*/
    
    @Override
    public void onBackPressed () {
        super.onBackPressed ();
        finish ();
//        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
    }
}