package com.actiknow.timesheet.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.dialog.RequestLeaveDialogFragment;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.Utils;
import com.bumptech.glide.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectDetailActivity extends AppCompatActivity {
    Bundle savedInstanceState;

    private AppBarLayout appBar;
    private RelativeLayout toolbarLayout;
    private RelativeLayout rlBack;
    private ImageView ivBack;
    private TextView tvTitle;
    private LinearLayout rlMain;
    private EditText etProjectName;
    private EditText etClientName;
    private EditText etStartDate;
    private EditText etEndDate;
    private EditText etProjectBudget;
    private EditText etProjectHourCost;
    private EditText etProjectAllottedHour;
    private EditText etProjectDescription;
    private TextView tvAddEmployee;
    String projects;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        initView();
        initData();
        initAdapter();
        initListener();
        setData();
    }

    private void initListener() {

        tvAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                RequestLeaveDialogFragment fragment = RequestLeaveDialogFragment.newInstance();
                fragment.show(ft, "test");
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

        ivBack = (ImageView) findViewById(R.id.ivBack);


        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbarLayout = (RelativeLayout) findViewById(R.id.toolbar_layout);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
      //  llFields = (LinearLayout)findViewById( R.id.llFields );
        etProjectName = (EditText)findViewById( R.id.etProjectName );
        etClientName = (EditText)findViewById( R.id.etClientName );
        etStartDate = (EditText)findViewById( R.id.etStartDate );
        etEndDate = (EditText)findViewById( R.id.etEndDate );
        etProjectBudget = (EditText)findViewById( R.id.etProjectBudget );
        etProjectHourCost = (EditText)findViewById( R.id.etProjectHourCost );
        etProjectAllottedHour = (EditText)findViewById( R.id.etProjectAllottedHour );
        etProjectDescription = (EditText)findViewById( R.id.etProjectDescription );
        tvAddEmployee = (TextView)findViewById( R.id.tvAddEmployee );
    }

    private void initData() {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initAdapter() {

    }


    private void setData() {
        projects = getIntent().getExtras().getString("allProject");
        id = getIntent().getExtras().getInt("position", 0);
        Log.d("projectId","000"+id);
        try {
            JSONArray jsonArray = new JSONArray(projects);
            if (jsonArray.length() > 0) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                   if (j==id){
                       etProjectName .setText(jsonObject.getString(AppConfigTags.PROJECT_TITLE));
                       etProjectDescription.setText(jsonObject.getString(AppConfigTags.PROJECT_DESCRIPTION));
                       etProjectBudget .setText(jsonObject.getString(AppConfigTags.PROJECT_BUDGET));
                       etProjectHourCost .setText(jsonObject.getString(AppConfigTags.PROJECT_HOUR_COST));
                       etStartDate.setText(Utils.dateFormat(jsonObject.getString(AppConfigTags.PROJECT_STARTED_AT)));
                       etEndDate.setText(Utils.dateFormat(jsonObject.getString(AppConfigTags.PROJECT_COMPLETE_AT)));
                       etProjectAllottedHour.setText(jsonObject.getString(AppConfigTags.PROJECT_ALLOTED_HOUR));
                   }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
