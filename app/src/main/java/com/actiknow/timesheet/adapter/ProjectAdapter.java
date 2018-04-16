package com.actiknow.timesheet.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.Project;
import com.actiknow.timesheet.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;

    private Activity activity;
    private List<Project> projectList = new ArrayList<>();

    public ProjectAdapter(Activity activity, List<Project> projectList) {
        this.activity = activity;
        this.projectList = projectList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.list_item_project, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final Project project = projectList.get(position);
        Utils.setTypefaceToAllViews (activity, holder.tvProjectName);
        holder.tvProjectName.setText (project.getProject_title ());
        holder.tvProjectClient.setText ("Client : " + project.getClient_name ());
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvProjectName;
        TextView tvProjectClient;
    
        public ViewHolder(View view) {
            super(view);
            tvProjectName = (TextView) view.findViewById (R.id.tvProjectName);
            tvProjectClient = (TextView) view.findViewById (R.id.tvProjectClient);
            view.setOnClickListener (this);
        }

        @Override
        public void onClick(View v) {
           mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}


