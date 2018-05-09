package com.actiknow.timesheet.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.SetTypeFace;
import com.actiknow.timesheet.utils.Utils;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RequestLeaveDialogFragment extends DialogFragment {
    ImageView ivCancel;
    TextView tvNumberLeave;
    LinearLayout ll2;
    CoordinatorLayout clMain;
    ImageView ivMinus;
    ImageView ivPlus;
    ImageView ivSave;
    int button1;
    TextView tvStartDate;
    TextView tvEndDate;
    TextView tvCounterDescription;
    TextView etLeaveType;
    String start_date = "";
    String end_date = "";
    EditText etDescription;
    WebView webView;
    String leave_type;
    ArrayList<String> typelist = new ArrayList<> ();
    private int mYear, mMonth, mDay;
    
    public static RequestLeaveDialogFragment newInstance () {
        RequestLeaveDialogFragment f = new RequestLeaveDialogFragment ();
        Bundle args = new Bundle ();
//        args.putString (AppConfigTags.LEAVES, leaves_json);
        f.setArguments (args);
        return f;
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
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (getActivity (), R.color.text_color_white));
        }
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
        ivSave = (ImageView) root.findViewById (R.id.ivSave);
        etLeaveType = (TextView) root.findViewById (R.id.etLeaveType);
        tvCounterDescription = (TextView) root.findViewById (R.id.tvCounterDescription);
        etDescription = (EditText) root.findViewById (R.id.etDescription);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        leave_type = bundle.getString (AppConfigTags.LEAVE_TYPE);
    }
    
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), clMain);
        tvNumberLeave.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
    
                int i = Integer.valueOf (tvNumberLeave.getText ().toString ());
    
                if (i > 0 && i == 1) {
                    ll2.setVisibility (View.VISIBLE);
                    tvStartDate.setVisibility (View.VISIBLE);
                    tvEndDate.setVisibility (View.GONE);
                } else if (i > 1) {
                    ll2.setVisibility (View.VISIBLE);
                    tvEndDate.setVisibility (View.VISIBLE);
                    tvStartDate.setVisibility (View.VISIBLE);
                } else {
                    ll2.setVisibility (View.GONE);
                    tvEndDate.setVisibility (View.GONE);
                    tvStartDate.setVisibility (View.GONE);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                
                // TODO Auto-generated method stub
            }
            
            @Override
            public void afterTextChanged (Editable s) {
                
                // TODO Auto-generated method stub
            }
        });
        
        /*
        try {
            JSONArray jsonArrayLeaveType = new JSONArray (leave_type);
            Log.e ("Type Id", "" + jsonArrayLeaveType.length ());
            for (int j = 0; j < jsonArrayLeaveType.length (); j++) {
                JSONObject jsonObject = jsonArrayLeaveType.getJSONObject (j);
                typelist.add (j, jsonObject.getString (AppConfigTags.TYPE_NAME));
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        */
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    
        ivMinus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (button1 > 0) {
                    button1--;
                    tvNumberLeave.setText (String.valueOf (button1));
                }
            }
        });
        ivPlus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                button1++;
                tvNumberLeave.setText (String.valueOf (button1));
            }
        });
    
        etLeaveType.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                new MaterialDialog.Builder (getActivity ())
                        .items (typelist)
                        .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                        .itemsCallback (new MaterialDialog.ListCallback () {
                            @Override
                            public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                Log.e ("Type Id", "" + typelist.indexOf (text));
                            }
                        })
                        .show ();
            }
        });
    
        etDescription.addTextChangedListener (new TextWatcher () {
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
    
        tvStartDate.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                selectDate (tvStartDate, 1);
            }
        });
        tvEndDate.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                selectDate (tvEndDate, 2);


              /*  Calendar cal1 = new GregorianCalendar();
                Calendar cal2 = new GregorianCalendar();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                Date date = null;
                try {
                    date = sdf.parse(tvStartDate.getText().toString());
                    cal1.setTime(date);
                    date = sdf.parse(tvEndDate.getText().toString());
                    cal2.setTime(date);

                    //cal1.set(2008, 8, 1);
                    //cal2.set(2008, 9, 31);

                    int totalDay= daysBetween(cal1.getTime(),cal2.getTime());
                    System.out.println("Days= "+(totalDay+1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/
            }
        });
    
        ivSave.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
            
            
            }
        });
    
    
    }
    
    
    public int daysBetween (Date d1, Date d2) {
        return (int) ((d2.getTime () - d1.getTime ()) / (1000 * 60 * 60 * 24));
    }
    
    
    private void selectDate (final TextView tvPickupDate, final int i) {
        final Calendar c = Calendar.getInstance ();
        mYear = c.get (Calendar.YEAR);
        mMonth = c.get (Calendar.MONTH);
        mDay = c.get (Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog (getActivity (), new DatePickerDialog.OnDateSetListener () {
            @Override
            public void onDateSet (DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (i == 1) {
                    start_date = year + "-" + String.format ("%02d", monthOfYear + 1) + "-" + String.format ("%02d", dayOfMonth);
                } else if (i == 2) {
                    end_date = year + "-" + String.format ("%02d", monthOfYear + 1) + "-" + String.format ("%02d", dayOfMonth);
                    
                    
                }
                tvPickupDate.setText (String.format ("%02d", dayOfMonth) + "-" + String.format ("%02d", monthOfYear + 1) + "-" + year);
                
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show ();
    }
    
    
}
