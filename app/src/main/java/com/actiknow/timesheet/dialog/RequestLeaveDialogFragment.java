package com.actiknow.timesheet.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.utils.Utils;

public class RequestLeaveDialogFragment extends DialogFragment {
    ImageView ivCancel;
    EditText etRequestDescription;
    LinearLayout ll2;
    CoordinatorLayout clMain;
    
    public static RequestLeaveDialogFragment newInstance(){
        RequestLeaveDialogFragment f = new RequestLeaveDialogFragment();
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
        ivCancel = (ImageView) root.findViewById(R.id.ivCancel);
        etRequestDescription = (EditText) root.findViewById(R.id.etRequestDescription);
        ll2 = (LinearLayout) root.findViewById(R.id.ll2);
        clMain=(CoordinatorLayout)root.findViewById(R.id.clMain);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity(), clMain);
        etRequestDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etRequestDescription.getText().toString().length() > 0){
                    int i = Integer.valueOf(etRequestDescription.getText().toString());
                    if(i > 1){
                        ll2.setVisibility(View.VISIBLE);
                    }else{
                        ll2.setVisibility(View.GONE);
                    }
                }else{
                    ll2.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });

    }
}