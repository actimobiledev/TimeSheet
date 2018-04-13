package com.actiknow.timesheet.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.dialog.RequestLeaveDialogFragment;
import com.actiknow.timesheet.utils.Utils;

public class LeaveActivity extends AppCompatActivity {
    Bundle savedInstanceState;
    TextView tvRequestLeave;
    TextView tvAcceptLeave;
    ImageView ivBack;
    CoordinatorLayout clMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        initView();
        initData();
        initAdapter();
        initListener();
    }

    private void initListener() {

        tvRequestLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                RequestLeaveDialogFragment fragment = RequestLeaveDialogFragment.newInstance ();
                fragment.show (ft, "test");
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void initView() {
        tvRequestLeave = (TextView)findViewById(R.id.tvRequestLeave);
        tvAcceptLeave = (TextView)findViewById(R.id.tvAcceptLeave);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        clMain=(CoordinatorLayout)findViewById(R.id.clMain);
    }

    private void initData() {
        Utils.setTypefaceToAllViews (this, clMain);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initAdapter() {

    }

}
