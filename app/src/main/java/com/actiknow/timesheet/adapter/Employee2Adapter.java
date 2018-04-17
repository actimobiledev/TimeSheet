package com.actiknow.timesheet.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.Employee2;
import com.actiknow.timesheet.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class Employee2Adapter extends RecyclerView.Adapter<Employee2Adapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    
    private Activity activity;
    private List<Employee2> employee2List = new ArrayList<> ();
    
    public Employee2Adapter (Activity activity, List<Employee2> employee2List) {
        this.activity = activity;
        this.employee2List = employee2List;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_employee2, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final Employee2 assignEmployee = employee2List.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvName);
        
        holder.tvName.setText (assignEmployee.getEmployee_name ());
        holder.tvRole.setText (assignEmployee.getRole ());
    }
    
    @Override
    public int getItemCount () {
        return employee2List.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvRole;
        
        public ViewHolder (View view) {
            super (view);
            tvName = (TextView) view.findViewById (R.id.tvName);
            tvRole = (TextView) view.findViewById (R.id.tvRole);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
    
    
}


