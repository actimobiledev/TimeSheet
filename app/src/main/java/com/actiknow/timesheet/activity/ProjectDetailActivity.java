package com.actiknow.timesheet.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.adapter.Employee2Adapter;
import com.actiknow.timesheet.model.Employee2;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//created by rahul jain 12/04/2018

public class ProjectDetailActivity extends AppCompatActivity {
    Bundle savedInstanceState;
    String projects;
    int id;
    int project_id = 0;
    CoordinatorLayout clMain;
    List<Employee2> employee2List = new ArrayList<> ();
    Employee2Adapter assignEmployeeAdapter;
    private RelativeLayout rlBack;
    private TextView tvClientName;
    private TextView tvProjectName;
    private TextView tvProjectDescription;
    private RecyclerView rvEmployees;
    private TextView tvAddEmployee;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_project_detail);
        initView ();
        initData ();
        initAdapter ();
        initListener ();
        setData ();
    }
    
    private void initListener () {
        tvAddEmployee.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
//                FragmentTransaction ft = getFragmentManager ().beginTransaction ();
//                EmployeeListDialogFragment fragment = EmployeeListDialogFragment.newInstance (project_id);
//                fragment.setDismissListener (new ProjectDetailActivity.MyDialogCloseListener3 () {
//                    @Override
//                    public void handleDialogClose (DialogInterface dialog) {
//
//                    }
//                });
//                fragment.show (ft, "test");
            }
        });
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        tvProjectName = (TextView) findViewById (R.id.tvProjectName);
        tvClientName = (TextView) findViewById (R.id.tvClientName);
        tvProjectDescription = (TextView) findViewById (R.id.tvProjectDescription);
        tvAddEmployee = (TextView) findViewById (R.id.tvAddEmployee);
        rvEmployees = (RecyclerView) findViewById (R.id.rvEmployees);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, clMain);
    }
    
    private void initAdapter () {
//        assignEmployeeAdapter = new AssignEmployeeAdapter (ProjectDetailActivity.this, assignEmployeeList);
//        rvAssignedEmployeeList.setAdapter (assignEmployeeAdapter);
//        rvAssignedEmployeeList.setHasFixedSize (true);
//        rvAssignedEmployeeList.addItemDecoration (new SimpleDividerItemDecoration (ProjectDetailActivity.this));
//        rvAssignedEmployeeList.setLayoutManager (new LinearLayoutManager (ProjectDetailActivity.this, LinearLayoutManager.VERTICAL, false));
//        rvAssignedEmployeeList.setItemAnimator (new DefaultItemAnimator ());
    }
    
    private void setData () {
        projects = getIntent ().getExtras ().getString (AppConfigTags.PROJECT);
        try {
            JSONObject jsonObject = new JSONObject (projects);
            project_id = jsonObject.getInt (AppConfigTags.PROJECT_ID);
            tvClientName.setText (jsonObject.getString (AppConfigTags.CLIENT_NAME));
            tvProjectName.setText (jsonObject.getString (AppConfigTags.PROJECT_TITLE));
            tvProjectDescription.setText (jsonObject.getString (AppConfigTags.PROJECT_DESCRIPTION));
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
    
    public interface MyDialogCloseListener3 {
        public void handleDialogClose (DialogInterface dialog);
    }
    
}
