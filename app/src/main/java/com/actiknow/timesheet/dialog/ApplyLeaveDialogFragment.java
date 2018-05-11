package com.actiknow.timesheet.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.LeaveType;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ApplyLeaveDialogFragment extends DialogFragment {
    OnDialogResultListener onDialogResultListener;
    ImageView ivCancel;
    ImageView ivSend;
    TextView tvFullDay;
    TextView tvHalfDay;
    TextView tvMultiple;
    TextView tvLeaves;
    ImageView ivMinus;
    ImageView ivPlus;
    LinearLayout llLeaveDates;
    EditText etLeaveType;
    EditText etLeaveFrom;
    EditText etLeaveTill;
    EditText etLeaveDescription;
    TextView tvCounterDescription;
    
    RelativeLayout rlMultiple;
    ProgressDialog progressDialog;
    AppDetailsPref appDetailsPref;
    
    
    List<String> leave_types = new ArrayList<String> ();
    List<LeaveType> typeList = new ArrayList<LeaveType> ();
    String leave_from = "";
    String leave_till = "";
    double leaves = 0;
    double leaves_remaining = 0;
    int type_id = 0;
    int type_status = 0;
    private int mYear, mMonth, mDay;
    
    public static ApplyLeaveDialogFragment newInstance () {
        ApplyLeaveDialogFragment fragment = new ApplyLeaveDialogFragment ();
        Bundle args = new Bundle ();
//        args.putString (AppConfigTags.PROJECTS, projects_json);
        fragment.setArguments (args);
        return fragment;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setStyle (DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }
    
    @Override
    public void onActivityCreated (Bundle arg0) {
        super.onActivityCreated (arg0);
        Window window = getDialog ().getWindow ();
        window.getAttributes ().windowAnimations = R.style.DialogAnimation;
//        if (Build.VERSION.SDK_INT >= 21) {
//            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor (ContextCompat.getColor (getActivity (), R.color.text_color_white));
//        }
    }
    
    @Override
    public void onResume () {
        super.onResume ();
    }
    
    @Override
    public void onStart () {
        super.onStart ();
        Dialog d = getDialog ();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow ().setLayout (width, height);
        }
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.fragment_dialog_request_leave, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        ivSend = (ImageView) root.findViewById (R.id.ivSend);
        tvFullDay = (TextView) root.findViewById (R.id.tvFullDay);
        tvHalfDay = (TextView) root.findViewById (R.id.tvHalfDay);
        tvMultiple = (TextView) root.findViewById (R.id.tvMultiple);
        tvLeaves = (TextView) root.findViewById (R.id.tvLeaves);
        ivMinus = (ImageView) root.findViewById (R.id.ivMinus);
        ivPlus = (ImageView) root.findViewById (R.id.ivPlus);
        rlMultiple = (RelativeLayout) root.findViewById (R.id.rlMultiple);
        llLeaveDates = (LinearLayout) root.findViewById (R.id.llLeaveDates);
    
        etLeaveFrom = (EditText) root.findViewById (R.id.etLeaveFrom);
        etLeaveTill = (EditText) root.findViewById (R.id.etLeaveTill);
    
        etLeaveType = (EditText) root.findViewById (R.id.etLeaveType);
        etLeaveDescription = (EditText) root.findViewById (R.id.etLeaveDescription);
        tvCounterDescription = (TextView) root.findViewById (R.id.tvCounterDescription);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
//        projects_json = bundle.getString (AppConfigTags.PROJECTS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), ivCancel);
        progressDialog = new ProgressDialog (getActivity ());
        appDetailsPref = AppDetailsPref.getInstance ();
    
        final Calendar c = Calendar.getInstance ();
        mYear = c.get (Calendar.YEAR);
        mMonth = c.get (Calendar.MONTH);
        mDay = c.get (Calendar.DAY_OF_MONTH);
    
    
        Log.e ("karman", appDetailsPref.getStringPref (getActivity (), AppDetailsPref.LEAVE_TYPES));
    
    
        try {
            JSONArray jsonArrayLeaveType = new JSONArray (appDetailsPref.getStringPref (getActivity (), AppDetailsPref.LEAVE_TYPES));
            for (int j = 0; j < jsonArrayLeaveType.length (); j++) {
                JSONObject jsonObject = jsonArrayLeaveType.getJSONObject (j);
                if (jsonObject.getInt (AppConfigTags.TYPE_STATUS) == 1) {
                    leave_types.add (jsonObject.getString (AppConfigTags.TYPE_NAME) + " (" + jsonObject.getDouble (AppConfigTags.REMAINING) + ")");
                } else {
                    leave_types.add (jsonObject.getString (AppConfigTags.TYPE_NAME));
                }
                typeList.add (new LeaveType (
                        jsonObject.getInt (AppConfigTags.TYPE_ID),
                        jsonObject.getString (AppConfigTags.TYPE_NAME),
                        jsonObject.getInt (AppConfigTags.TYPE_STATUS),
                        jsonObject.getDouble (AppConfigTags.TOTAL),
                        jsonObject.getDouble (AppConfigTags.AVAILED),
                        jsonObject.getDouble (AppConfigTags.REMAINING)
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        
        
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
        tvHalfDay.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                leave_from = "";
                leave_till = "";
                etLeaveFrom.setText ("");
                etLeaveTill.setText ("");
    
                if (type_id != 0) {
                    switch (type_status) {
                        case 1:
                            if (leaves_remaining >= 0.5) {
                                tvHalfDay.setTextColor (getActivity ().getResources ().getColor (R.color.text_color_white));
                                tvHalfDay.setBackgroundResource (R.drawable.bt_filled);
    
                                tvFullDay.setTextColor (getActivity ().getResources ().getColor (R.color.primary_text));
                                tvFullDay.setBackgroundResource (R.drawable.bt_empty);
                                rlMultiple.setBackgroundResource (R.drawable.bt_empty);
                                tvMultiple.setVisibility (View.VISIBLE);
                                tvLeaves.setVisibility (View.GONE);
                                ivPlus.setVisibility (View.GONE);
                                ivMinus.setVisibility (View.GONE);
    
                                llLeaveDates.setVisibility (View.VISIBLE);
                                leaves = 0.5;
                            } else {
                                Utils.showToast (getActivity (), "You don't have leaves available", false);
                            }
                            break;
                        case 2:
                            tvHalfDay.setTextColor (getActivity ().getResources ().getColor (R.color.text_color_white));
                            tvHalfDay.setBackgroundResource (R.drawable.bt_filled);
    
                            tvFullDay.setTextColor (getActivity ().getResources ().getColor (R.color.primary_text));
                            tvFullDay.setBackgroundResource (R.drawable.bt_empty);
                            rlMultiple.setBackgroundResource (R.drawable.bt_empty);
                            tvMultiple.setVisibility (View.VISIBLE);
                            tvLeaves.setVisibility (View.GONE);
                            ivPlus.setVisibility (View.GONE);
                            ivMinus.setVisibility (View.GONE);
    
                            llLeaveDates.setVisibility (View.VISIBLE);
                            leaves = 0.5;
                            break;
                    }
                } else {
                    Utils.showToast (getActivity (), "Please select a leave type", false);
                }
            }
        });
    
        tvFullDay.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                leave_from = "";
                leave_till = "";
                etLeaveFrom.setText ("");
                etLeaveTill.setText ("");
    
                if (type_id != 0) {
                    switch (type_status) {
                        case 1:
                            if (leaves_remaining >= 1.0) {
                                tvFullDay.setTextColor (getActivity ().getResources ().getColor (R.color.text_color_white));
                                tvFullDay.setBackgroundResource (R.drawable.bt_filled);
    
                                tvHalfDay.setTextColor (getActivity ().getResources ().getColor (R.color.primary_text));
                                tvHalfDay.setBackgroundResource (R.drawable.bt_empty);
                                rlMultiple.setBackgroundResource (R.drawable.bt_empty);
                                tvMultiple.setVisibility (View.VISIBLE);
                                tvLeaves.setVisibility (View.GONE);
                                ivPlus.setVisibility (View.GONE);
                                ivMinus.setVisibility (View.GONE);
    
                                llLeaveDates.setVisibility (View.VISIBLE);
                                leaves = 1.0;
                            } else {
                                Utils.showToast (getActivity (), "You don't have leaves available", false);
                            }
                            break;
                        case 2:
                            tvFullDay.setTextColor (getActivity ().getResources ().getColor (R.color.text_color_white));
                            tvFullDay.setBackgroundResource (R.drawable.bt_filled);
    
                            tvHalfDay.setTextColor (getActivity ().getResources ().getColor (R.color.primary_text));
                            tvHalfDay.setBackgroundResource (R.drawable.bt_empty);
                            rlMultiple.setBackgroundResource (R.drawable.bt_empty);
                            tvMultiple.setVisibility (View.VISIBLE);
                            tvLeaves.setVisibility (View.GONE);
                            ivPlus.setVisibility (View.GONE);
                            ivMinus.setVisibility (View.GONE);
    
                            llLeaveDates.setVisibility (View.VISIBLE);
                            leaves = 1.0;
                            break;
                    }
                } else {
                    Utils.showToast (getActivity (), "Please select a leave type", false);
                }
            }
        });
    
        rlMultiple.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                leave_from = "";
                leave_till = "";
                etLeaveFrom.setText ("");
                etLeaveTill.setText ("");
    
                if (type_id != 0) {
                    switch (type_status) {
                        case 1:
                            if (leaves_remaining >= 2.0) {
                                rlMultiple.setBackgroundResource (R.drawable.bt_filled);
                                tvMultiple.setVisibility (View.GONE);
                                tvLeaves.setVisibility (View.VISIBLE);
                                ivPlus.setVisibility (View.VISIBLE);
                                ivMinus.setVisibility (View.VISIBLE);
    
                                tvHalfDay.setTextColor (getActivity ().getResources ().getColor (R.color.primary_text));
                                tvFullDay.setTextColor (getActivity ().getResources ().getColor (R.color.primary_text));
                                tvHalfDay.setBackgroundResource (R.drawable.bt_empty);
                                tvFullDay.setBackgroundResource (R.drawable.bt_empty);
    
                                llLeaveDates.setVisibility (View.VISIBLE);
                                leaves = 2.0;
                                tvLeaves.setText ("2.0");
                            } else {
                                Utils.showToast (getActivity (), "You don't have leaves available", false);
                            }
                            break;
                        case 2:
                            rlMultiple.setBackgroundResource (R.drawable.bt_filled);
                            tvMultiple.setVisibility (View.GONE);
                            tvLeaves.setVisibility (View.VISIBLE);
                            ivPlus.setVisibility (View.VISIBLE);
                            ivMinus.setVisibility (View.VISIBLE);
    
                            tvHalfDay.setTextColor (getActivity ().getResources ().getColor (R.color.primary_text));
                            tvFullDay.setTextColor (getActivity ().getResources ().getColor (R.color.primary_text));
                            tvHalfDay.setBackgroundResource (R.drawable.bt_empty);
                            tvFullDay.setBackgroundResource (R.drawable.bt_empty);
    
                            llLeaveDates.setVisibility (View.VISIBLE);
                            leaves = 2.0;
                            tvLeaves.setText ("2.0");
                            break;
                    }
                } else {
                    Utils.showToast (getActivity (), "Please select a leave type", false);
                }
            }
        });
        
        ivPlus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                double leave = Double.parseDouble (tvLeaves.getText ().toString ());
                switch (type_status) {
                    case 1:
                        if (leaves_remaining > leave) {
                            leave = leave + 0.5;
                            leaves = leave;
                            tvLeaves.setText (String.valueOf (leave));
    
                            leave_from = "";
                            leave_till = "";
                            etLeaveFrom.setText ("");
                            etLeaveTill.setText ("");
    
                        } else {
                            Utils.showToast (getActivity (), "You don't have leaves available", false);
                        }
                        break;
                    case 2:
                        leave = leave + 0.5;
                        leaves = leave;
                        tvLeaves.setText (String.valueOf (leave));
    
                        leave_from = "";
                        leave_till = "";
                        etLeaveFrom.setText ("");
                        etLeaveTill.setText ("");
    
                        break;
                }
            }
        });
        
        ivMinus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                double leave = Double.parseDouble (tvLeaves.getText ().toString ());
                if (leave > 2.0) {
                    leave = leave - 0.5;
                    leaves = leave;
                    tvLeaves.setText (String.valueOf (leave));
    
                    leave_from = "";
                    leave_till = "";
                    etLeaveFrom.setText ("");
                    etLeaveTill.setText ("");
                }
            }
        });
    
        etLeaveDescription.addTextChangedListener (new TextWatcher () {
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
    
        etLeaveType.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                new MaterialDialog.Builder (getActivity ())
                        .items (leave_types)
                        .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                        .itemsCallback (new MaterialDialog.ListCallback () {
                            @Override
                            public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                LeaveType leaveType = typeList.get (which);
    
                                tvHalfDay.setEnabled (true);
                                tvFullDay.setEnabled (true);
                                rlMultiple.setEnabled (true);
                                
                                tvHalfDay.setTextColor (getActivity ().getResources ().getColor (R.color.primary_text));
                                tvFullDay.setTextColor (getActivity ().getResources ().getColor (R.color.primary_text));
                                rlMultiple.setBackgroundResource (R.drawable.bt_empty);
                                tvHalfDay.setBackgroundResource (R.drawable.bt_empty);
                                tvFullDay.setBackgroundResource (R.drawable.bt_empty);
                                tvMultiple.setVisibility (View.VISIBLE);
                                tvLeaves.setVisibility (View.GONE);
                                ivPlus.setVisibility (View.GONE);
                                ivMinus.setVisibility (View.GONE);
    
                                llLeaveDates.setVisibility (View.GONE);
                                etLeaveFrom.setText ("");
                                etLeaveTill.setText ("");
    
                                type_id = leaveType.getType_id ();
                                type_status = leaveType.getType_status ();
                                leaves = 0;
                                leave_from = "";
                                leave_till = "";
    
                                if (leaveType.getType_name ().contains ("Short")) {
                                    MaterialDialog dialog2 = new MaterialDialog.Builder (getActivity ())
                                            .content ("You can only select full day as duration for Short Leave")
                                            .titleColor (getResources ().getColor (R.color.primary_text))
                                            .positiveColor (getResources ().getColor (R.color.primary_text))
                                            .contentColor (getResources ().getColor (R.color.primary_text))
                                            .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                                            .canceledOnTouchOutside (true)
                                            .cancelable (true)
                                            .positiveText (R.string.dialog_action_ok)
                                            .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                @Override
                                                public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    tvHalfDay.setEnabled (false);
                                                    rlMultiple.setEnabled (false);
                                                    leaves = 1.0;
                        
                                                    tvFullDay.setTextColor (getActivity ().getResources ().getColor (R.color.text_color_white));
                                                    tvFullDay.setBackgroundResource (R.drawable.bt_filled);
                                                    llLeaveDates.setVisibility (View.VISIBLE);
                                                }
                                            }).build ();
                                    dialog2.show ();
                                }
    
                                if (leaveType.getType_name ().contains ("Home")) {
                                    MaterialDialog dialog2 = new MaterialDialog.Builder (getActivity ())
                                            .content ("Work from home is currently not supported from the app right now.")
                                            .titleColor (getResources ().getColor (R.color.primary_text))
                                            .positiveColor (getResources ().getColor (R.color.primary_text))
                                            .contentColor (getResources ().getColor (R.color.primary_text))
                                            .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                                            .canceledOnTouchOutside (true)
                                            .cancelable (true)
                                            .positiveText (R.string.dialog_action_ok)
                                            .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                @Override
                                                public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    tvHalfDay.setEnabled (false);
                                                    tvFullDay.setEnabled (false);
                                                    rlMultiple.setEnabled (false);
                                                }
                                            }).build ();
                                    dialog2.show ();
                                }
    
    
                                switch (leaveType.getType_status ()) {
                                    case 1:
                                        if (leaveType.getRemaining () > 0) {
                                            leaves_remaining = leaveType.getRemaining ();
                                            etLeaveType.setText (text.toString ());
                                            etLeaveType.setError (null);
                                        } else {
                                            leaves_remaining = 0;
                                            etLeaveType.setText ("");
                                            Utils.showToast (getActivity (), "You don't have leaves available in " + leaveType.getType_name (), false);
                                        }
                                        break;
                                    case 2:
                                        etLeaveType.setText (text.toString ());
                                        etLeaveType.setError (null);
                                        break;
                                }
                            }
                        })
                        .show ();
            }
        });
    
    
        etLeaveFrom.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog (getActivity (), new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet (DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etLeaveFrom.setError (null);
                        etLeaveFrom.setText (String.format (Locale.US, "%02d", dayOfMonth) + "/" + String.format (Locale.US, "%02d", monthOfYear + 1) + "/" + year);
                        leave_from = Utils.convertTimeFormat (etLeaveFrom.getText ().toString ().trim (), "dd/MM/yyyy", "yyyy-MM-dd");
    
                        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd", Locale.US);
                        SimpleDateFormat sdf1 = new SimpleDateFormat ("dd/MM/yyyy", Locale.US);
    
                        Calendar c = Calendar.getInstance ();
                        try {
                            c.setTime (sdf.parse (leave_from));
                        } catch (ParseException e) {
                            e.printStackTrace ();
                        }
                        c.add (Calendar.DATE, (int) Math.ceil (leaves - 1));
    
                        etLeaveTill.setText (sdf1.format (c.getTime ()));
                        leave_till = Utils.convertTimeFormat (etLeaveTill.getText ().toString ().trim (), "dd/MM/yyyy", "yyyy-MM-dd");
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show ();
            }
        });
    
        ivSend.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Utils.hideSoftKeyboard (getActivity ());
                SpannableString s1 = new SpannableString (getResources ().getString (R.string.please_select_leave_type));
                s1.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s1.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.please_select_dates));
                s2.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s2.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString (getResources ().getString (R.string.please_specify_reason));
                s3.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    
                if (type_id == 0) {
                    etLeaveType.setError (s1);
                }
                if (leave_from.length () == 0) {
                    etLeaveFrom.setError (s2);
                }
                if (etLeaveDescription.getText ().toString ().length () == 0) {
                    etLeaveDescription.setError (s3);
                }
    
                if (type_id != 0 && leaves != 0 && leave_from.length () > 0 && leave_till.length () > 0 && etLeaveDescription.getText ().toString ().length () > 0 && etLeaveDescription.getText ().toString ().length () <= 256) {
                    sendLeaveRequestToServer (type_id, leaves, leave_from, leave_till, etLeaveDescription.getText ().toString ().trim ());
                }
            }
        });
    }
    
    
    public void setOnDialogResultListener (OnDialogResultListener listener) {
        this.onDialogResultListener = listener;
    }
    
    @Override
    public void onDismiss (DialogInterface dialog) {
        super.onDismiss (dialog);
        if (onDialogResultListener != null) {
            onDialogResultListener.onNegativeResult ();
            dialog.cancel ();
        }
    }
    
    private void sendLeaveRequestToServer (final int type_id, final double leaves, final String leave_from, final String leave_till, final String description) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (getActivity (), progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_APPLY_LEAVE, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_APPLY_LEAVE,
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
                                        new MaterialDialog.Builder (getActivity ())
                                                .content (message)
                                                .positiveText ("OK")
                                                .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                                                .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                    @Override
                                                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        getDialog ().dismiss ();
                                                    }
                                                })
                                                .show ();
                                    } else {
                                        Utils.showSnackBar (getActivity (), rlMultiple, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (getActivity (), rlMultiple, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity (), rlMultiple, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (getActivity (), rlMultiple, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.TYPE_ID, String.valueOf (type_id));
                    params.put (AppConfigTags.LEAVES, String.valueOf (leaves));
                    params.put (AppConfigTags.LEAVE_FROM, leave_from);
                    params.put (AppConfigTags.LEAVE_TILL, leave_till);
                    params.put (AppConfigTags.DESCRIPTION, description);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (getActivity (), AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (getActivity (), rlMultiple, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    public interface OnDialogResultListener {
        public abstract void onPositiveResult ();
        
        public abstract void onNegativeResult ();
    }
}