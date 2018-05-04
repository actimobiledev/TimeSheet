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

import java.util.ArrayList;
import java.util.List;


public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder> {
    private OnItemClickListener mItemClickListener;
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
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Leave leave = leaveList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvType);
        holder.tvType.setText (leave.getType_name ());
        holder.tvAvailable.setText (leave.getRemaining ());
        holder.tvAvailed.setText (leave.getAvailed ());
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