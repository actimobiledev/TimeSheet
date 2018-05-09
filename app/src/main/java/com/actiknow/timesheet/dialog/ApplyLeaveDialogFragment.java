package com.actiknow.timesheet.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Utils;

public class ApplyLeaveDialogFragment extends DialogFragment {
    OnDialogResultListener onDialogResultListener;
    ImageView ivCancel;
    TextView tvFullDay;
    TextView tvHalfDay;
    TextView tvMultiple;
    TextView tvLeaves;
    ImageView ivMinus;
    ImageView ivPlus;
    LinearLayout llLeaveDates;
    
    RelativeLayout rlMultiple;
    ProgressDialog progressDialog;
    AppDetailsPref appDetailsPref;
    
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
        tvFullDay = (TextView) root.findViewById (R.id.tvFullDay);
        tvHalfDay = (TextView) root.findViewById (R.id.tvHalfDay);
        tvMultiple = (TextView) root.findViewById (R.id.tvMultiple);
        tvLeaves = (TextView) root.findViewById (R.id.tvLeaves);
        ivMinus = (ImageView) root.findViewById (R.id.ivMinus);
        ivPlus = (ImageView) root.findViewById (R.id.ivPlus);
        rlMultiple = (RelativeLayout) root.findViewById (R.id.rlMultiple);
        llLeaveDates = (LinearLayout) root.findViewById (R.id.llLeaveDates);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
//        projects_json = bundle.getString (AppConfigTags.PROJECTS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), ivCancel);
        progressDialog = new ProgressDialog (getActivity ());
        appDetailsPref = AppDetailsPref.getInstance ();
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

//                new Handler ().postDelayed (new Runnable () {
//                    @Override
//                    public void run () {
//                    }
//                }, 0);
            }
        });
        tvFullDay.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
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

//                new Handler ().postDelayed (new Runnable () {
//                    @Override
//                    public void run () {
//                    }
//                }, 0);
                
            }
        });
        rlMultiple.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
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

//                new Handler ().postDelayed (new Runnable () {
//                    @Override
//                    public void run () {
//                    }
//                }, 0);
            }
        });
        
        ivPlus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                int leave = Integer.parseInt (tvLeaves.getText ().toString ());
                leave++;
                tvLeaves.setText (String.valueOf (leave));
            }
        });
        
        ivMinus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                int leave = Integer.parseInt (tvLeaves.getText ().toString ());
                if (leave > 2) {
                    leave--;
                    tvLeaves.setText (String.valueOf (leave));
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
        Utils.hideSoftKeyboard (getActivity ());
        if (onDialogResultListener != null) {
            onDialogResultListener.onNegativeResult ();
            dialog.cancel ();
        }
    }
    
    public interface OnDialogResultListener {
        public abstract void onPositiveResult ();
        
        public abstract void onNegativeResult ();
    }
}