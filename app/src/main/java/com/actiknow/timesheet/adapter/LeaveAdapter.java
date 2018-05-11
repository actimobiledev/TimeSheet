package com.actiknow.timesheet.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.Leave;
import com.actiknow.timesheet.utils.Utils;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    
    private Activity activity;
    private List<Leave> leaveList = new ArrayList<> ();
    
    public LeaveAdapter (Activity activity, List<Leave> leaveList) {
        this.activity = activity;
        this.leaveList = leaveList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_leave, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final Leave leave = leaveList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvType);
        holder.tvDescription.setText ("Reason : " + leave.getDescription ());
    
        if (leave.getLeaves_availed () > 1.0) {
            holder.tvType.setText ("Leave Type : " + leave.getType_name () + " (" + leave.getLeaves_availed () + " days)");
            holder.tvDates.setText ("Leave Dates : " + Utils.convertTimeFormat (leave.getLeave_from (), "yyyy-MM-dd", "dd") + " - " + Utils.convertTimeFormat (leave.getLeave_till (), "yyyy-MM-dd", "dd/MM/yyyy"));
        } else {
            holder.tvType.setText ("Leave Type : " + leave.getType_name () + " (" + leave.getLeaves_availed () + " day)");
            holder.tvDates.setText ("Leave Date : " + Utils.convertTimeFormat (leave.getLeave_from (), "yyyy-MM-dd", "dd/MM/yyyy"));
        }
    
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        try {
            holder.tvAppliedAt.setText ("Applied : " + TimeAgo.using (sdf.parse (leave.getApplied_at ()).getTime ()));
        } catch (ParseException e) {
            e.printStackTrace ();
        }
    
    
        //0=>Applied, 1=>Approved, 2=>Declined, 3=>Cancelled by user
        switch (leave.getStatus ()) {
            case 0:
                holder.tvStatus.setText ("Applied");
                holder.tvUpdatedBy.setText ("N/A");
                break;
            case 1:
                holder.tvStatus.setText ("Approved");
                holder.tvUpdatedBy.setText (leave.getUpdated_by ());
                if (leave.getRemark ().length () > 0) {
                    holder.tvRemark.setVisibility (View.VISIBLE);
                    holder.tvRemark.setText (leave.getRemark ());
                } else {
                    holder.tvRemark.setVisibility (View.GONE);
                }
                break;
            case 2:
                holder.tvStatus.setText ("Declined");
                holder.tvUpdatedBy.setText (leave.getUpdated_by ());
                if (leave.getRemark ().length () > 0) {
                    holder.tvRemark.setVisibility (View.VISIBLE);
                    holder.tvRemark.setText (leave.getRemark ());
                } else {
                    holder.tvRemark.setVisibility (View.GONE);
                }
                break;
            case 3:
                holder.tvStatus.setText ("Cancelled");
                holder.tvUpdatedBy.setText ("N/A");
                break;
        }
    }
    
    @Override
    public int getItemCount () {
        return leaveList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvType;
        TextView tvDates;
        TextView tvDescription;
        TextView tvAppliedAt;
        TextView tvStatus;
        TextView tvUpdatedBy;
        TextView tvRemark;
        
        
        public ViewHolder (View view) {
            super (view);
            tvType = (TextView) view.findViewById (R.id.tvType);
            tvAppliedAt = (TextView) view.findViewById (R.id.tvAppliedAt);
            tvDates = (TextView) view.findViewById (R.id.tvLeaveDates);
            tvDescription = (TextView) view.findViewById (R.id.tvDescription);
            tvStatus = (TextView) view.findViewById (R.id.tvStatus);
            tvUpdatedBy = (TextView) view.findViewById (R.id.tvUpdatedBy);
            tvRemark = (TextView) view.findViewById (R.id.tvRemark);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}


