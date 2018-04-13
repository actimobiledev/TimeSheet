package com.actiknow.timesheet.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.Project;
import com.actiknow.timesheet.utils.SetTypeFace;

import java.util.ArrayList;
import java.util.List;

import static com.actiknow.timesheet.utils.Utils.dateFormat;


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
    public void onBindViewHolder(final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);

        final Project project = projectList.get(position);
        holder.tvName.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvDescription.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvTime.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvName.setText(project.getProject_title());
        holder.tvDescription.setText(project.getDescription());
        holder.tvTime.setText("Start Project -"+dateFormat(project.getStarted_at()));
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
        TextView tvName;
        TextView tvDescription;
        TextView tvTime;

        ProgressBar progressBar;


        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById (R.id.tvName);
            tvDescription = (TextView) view.findViewById (R.id.tvDescription);
            tvTime = (TextView) view.findViewById (R.id.tvTime);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           mItemClickListener.onItemClick (v, getLayoutPosition ());


        }
    }
    
    
}


