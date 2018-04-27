package com.actiknow.timesheet.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.MyEmployeeProject;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MyEmployeeProjectAdapter extends RecyclerView.Adapter<MyEmployeeProjectAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    
    private Activity activity;
    private List<MyEmployeeProject> projectList = new ArrayList<> ();
    
    public MyEmployeeProjectAdapter (Activity activity, List<MyEmployeeProject> projectList) {
        this.activity = activity;
        this.projectList = projectList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_my_employee_project, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final MyEmployeeProject project = projectList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvProjectName);
        holder.tvProjectName.setText (project.getProject_title ());
        
        
        try {
            JSONArray jsonArray = new JSONArray (project.getHours_json ());
            for (int i = 0; i < jsonArray.length (); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject (i);
                Calendar c = Calendar.getInstance ();
                c.set (Calendar.DAY_OF_WEEK, c.MONDAY);
                DateFormat df = new SimpleDateFormat ("yyyy-MM-dd", Locale.US);
                DateFormat df3 = new SimpleDateFormat ("dd/MM", Locale.US);
                for (int j = 1; j < 8; j++) {
                    switch (j) {
                        case 1:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvMonTotal.setText ("" + jsonObject.getString (AppConfigTags.HOUR));
                            }
                            holder.tvMonDate.setText (df3.format (c.getTime ()));
                            break;
                        case 2:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvTueTotal.setText ("" + jsonObject.getString (AppConfigTags.HOUR));
                            }
                            holder.tvTueDate.setText (df3.format (c.getTime ()));
                            break;
                        case 3:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvWedTotal.setText ("" + jsonObject.getString (AppConfigTags.HOUR));
                            }
                            holder.tvWedDate.setText (df3.format (c.getTime ()));
                            break;
                        case 4:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvThurTotal.setText ("" + jsonObject.getString (AppConfigTags.HOUR));
                            }
                            holder.tvThurDate.setText (df3.format (c.getTime ()));
                            break;
                        case 5:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvFriTotal.setText ("" + jsonObject.getString (AppConfigTags.HOUR));
                            }
                            holder.tvFriDate.setText (df3.format (c.getTime ()));
                            break;
                        case 6:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvSatTotal.setText ("" + jsonObject.getString (AppConfigTags.HOUR));
                            }
                            holder.tvSatDate.setText (df3.format (c.getTime ()));
                            break;
                        case 7:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvSunTotal.setText ("" + jsonObject.getString (AppConfigTags.HOUR));
                            }
                            holder.tvSunDate.setText (df3.format (c.getTime ()));
                            break;
                    }
                    c.add (Calendar.DATE, 1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
    
    @Override
    public int getItemCount () {
        return projectList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvProjectName;
        
        TextView tvMonDate;
        TextView tvMonTotal;
        TextView tvTueDate;
        TextView tvTueTotal;
        TextView tvWedDate;
        TextView tvWedTotal;
        TextView tvThurDate;
        TextView tvThurTotal;
        TextView tvFriDate;
        TextView tvFriTotal;
        TextView tvSatDate;
        TextView tvSatTotal;
        TextView tvSunDate;
        TextView tvSunTotal;
        
        
        public ViewHolder (View view) {
            super (view);
            tvProjectName = (TextView) view.findViewById (R.id.tvName);
            
            tvMonDate = (TextView) view.findViewById (R.id.tvMonDate);
            tvMonTotal = (TextView) view.findViewById (R.id.tvMonTotal);
            tvTueDate = (TextView) view.findViewById (R.id.tvTueDate);
            tvTueTotal = (TextView) view.findViewById (R.id.tvTueTotal);
            tvWedDate = (TextView) view.findViewById (R.id.tvWedDate);
            tvWedTotal = (TextView) view.findViewById (R.id.tvWedTotal);
            tvThurDate = (TextView) view.findViewById (R.id.tvThurDate);
            tvThurTotal = (TextView) view.findViewById (R.id.tvThurTotal);
            tvFriDate = (TextView) view.findViewById (R.id.tvFriDate);
            tvFriTotal = (TextView) view.findViewById (R.id.tvFriTotal);
            tvSatDate = (TextView) view.findViewById (R.id.tvSatDate);
            tvSatTotal = (TextView) view.findViewById (R.id.tvSatTotal);
            tvSunDate = (TextView) view.findViewById (R.id.tvSunDate);
            tvSunTotal = (TextView) view.findViewById (R.id.tvSunTotal);
            
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}


