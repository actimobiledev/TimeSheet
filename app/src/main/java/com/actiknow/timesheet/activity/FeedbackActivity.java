package com.actiknow.timesheet.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppConfigURL;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Constants;
import com.actiknow.timesheet.utils.NetworkConnection;
import com.actiknow.timesheet.utils.SetTypeFace;
import com.actiknow.timesheet.utils.TypefaceSpan;
import com.actiknow.timesheet.utils.Utils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class FeedbackActivity extends AppCompatActivity {

    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    AppDetailsPref appDetailsPref;
    EditText etComment;

    TextView tvSubmit;
    RelativeLayout rlBack;


    Spinner spinner;
    ImageView ivStar1;
    ImageView ivStar2;
    ImageView ivStar3;
    ImageView ivStar4;
    ImageView ivStar5;
    int starValue=0;
    String spinnerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        Utils.setTypefaceToAllViews(this, clMain);
        progressDialog = new ProgressDialog(FeedbackActivity.this);
        //  Utils.setTypefaceToAllViews (this, clMain);
        appDetailsPref = AppDetailsPref.getInstance();


        List<String> report = new ArrayList<String>();
        report.add("Bug");
        report.add ("Crashed");
        report.add("Incorrect data");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, report);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void initView() {
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        etComment = (EditText) findViewById(R.id.etComment);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        ivStar1 = (ImageView) findViewById(R.id.ivStar1);
        ivStar2 = (ImageView) findViewById(R.id.ivStar2);
        ivStar3 = (ImageView) findViewById(R.id.ivStar3);
        ivStar4 = (ImageView) findViewById(R.id.ivStar4);
        ivStar5 = (ImageView) findViewById(R.id.ivStar5);
        spinner = (Spinner) findViewById(R.id.spinner);
    }

    private void initListener() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });








        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                spinnerItem= spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    
    
        etComment.addTextChangedListener (new TextWatcher() {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etComment.setError (null);
                }
            }
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged (Editable s) {
            }
        });

        tvSubmit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Utils.hideSoftKeyboard (FeedbackActivity.this);
                SpannableString s1 = new SpannableString("Please enter the comment");
                s1.setSpan (new TypefaceSpan(FeedbackActivity.this, Constants.font_name), 0, s1.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (etComment.getText ().toString ().trim ().length () == 0) {
                    etComment.setError (s1);
                }else {
                    sendFeedbackToServer();
                }

            }
        });


        ivStar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starValue=1;

                ivStar1.setImageResource(R.drawable.ic_filled_star);
                ivStar2.setImageResource(R.drawable.ic_star);
                ivStar3.setImageResource(R.drawable.ic_star);
                ivStar4.setImageResource(R.drawable.ic_star);
                ivStar5.setImageResource(R.drawable.ic_star);

            }
        });
        ivStar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starValue=2;
                ivStar1.setImageResource(R.drawable.ic_filled_star);
                ivStar2.setImageResource(R.drawable.ic_filled_star);
                ivStar3.setImageResource(R.drawable.ic_star);
                ivStar4.setImageResource(R.drawable.ic_star);
                ivStar5.setImageResource(R.drawable.ic_star);
            }
        });
        ivStar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starValue=3;
                ivStar1.setImageResource(R.drawable.ic_filled_star);
                ivStar2.setImageResource(R.drawable.ic_filled_star);
                ivStar3.setImageResource(R.drawable.ic_filled_star);
                ivStar4.setImageResource(R.drawable.ic_star);
                ivStar5.setImageResource(R.drawable.ic_star);
            }
        });
        ivStar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starValue=4;
                ivStar1.setImageResource(R.drawable.ic_filled_star);
                ivStar2.setImageResource(R.drawable.ic_filled_star);
                ivStar3.setImageResource(R.drawable.ic_filled_star);
                ivStar4.setImageResource(R.drawable.ic_filled_star);
                ivStar5.setImageResource(R.drawable.ic_star);
            }
        });
        ivStar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starValue=5;
                ivStar1.setImageResource(R.drawable.ic_filled_star);
                ivStar2.setImageResource(R.drawable.ic_filled_star);
                ivStar3.setImageResource(R.drawable.ic_filled_star);
                ivStar4.setImageResource(R.drawable.ic_filled_star);
                ivStar5.setImageResource(R.drawable.ic_filled_star);
            }
        });



    }
    
    
    private void sendFeedbackToServer () {
        if (NetworkConnection.isNetworkAvailable (FeedbackActivity.this)) {
            Utils.showProgressDialog(FeedbackActivity.this, progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_FEEDBACK, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_FEEDBACK,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        new MaterialDialog.Builder (FeedbackActivity.this)
                                                .content (message)
                                                .positiveText ("OK")
                                                .typeface (SetTypeFace.getTypeface (FeedbackActivity.this), SetTypeFace.getTypeface (FeedbackActivity.this))
                                                .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                    @Override
                                                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        dialog.dismiss ();
                                                        finish ();
                                                    }
                                                })
                                                .show ();
                                    } else {
                                        Utils.showSnackBar (FeedbackActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (FeedbackActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (FeedbackActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (FeedbackActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put (AppConfigTags.FEEDBACK_TYPE, spinnerItem);
                    params.put (AppConfigTags.RATING, String.valueOf(starValue));
                    params.put (AppConfigTags.DESCRIPTION, etComment.getText().toString().trim());


                    
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put(AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref(FeedbackActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
}