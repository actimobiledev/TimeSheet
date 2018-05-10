package com.actiknow.timesheet.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.LeaveType;
import com.actiknow.timesheet.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class LeaveTypeAdapter extends RecyclerView.Adapter<LeaveTypeAdapter.ViewHolder> {
    private OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<LeaveType> leaveTypeList = new ArrayList<> ();
    
    public LeaveTypeAdapter (Activity activity, List<LeaveType> leaveTypeList) {
        this.activity = activity;
        this.leaveTypeList = leaveTypeList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_leave_type, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final LeaveType leaveType = leaveTypeList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvType);
        holder.tvType.setText (leaveType.getType_name ());
        holder.tvAvailable.setText ("" + leaveType.getRemaining ());
        holder.tvAvailed.setText ("" + leaveType.getAvailed ());
    }
    
    @Override
    public int getItemCount () {
        return leaveTypeList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvType;
        TextView tvAvailable;
        TextView tvAvailed;
        
        public ViewHolder (View view) {
            super (view);
            tvType = (TextView) view.findViewById (R.id.tvType);
            tvAvailable = (TextView) view.findViewById (R.id.tvAvailable);
            tvAvailed = (TextView) view.findViewById (R.id.tvAvailed);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}