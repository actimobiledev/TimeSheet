package com.actiknow.timesheet.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.MyEmployee;
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


public class MyEmployeeAdapter extends RecyclerView.Adapter<MyEmployeeAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    
    private Activity activity;
    private List<MyEmployee> myEmployeeList = new ArrayList<> ();
    
    public MyEmployeeAdapter (Activity activity, List<MyEmployee> myEmployeeList) {
        this.activity = activity;
        this.myEmployeeList = myEmployeeList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_my_employee, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final MyEmployee myEmployee = myEmployeeList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvName);
        
        holder.tvName.setText (myEmployee.getEmployee_name ());
        
        try {
            JSONArray jsonArray = new JSONArray (myEmployee.getTotal_json ());
            for (int i = 0; i < jsonArray.length (); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject (i);
                Calendar c = Calendar.getInstance ();
                c.set (Calendar.DAY_OF_WEEK, c.MONDAY);
                DateFormat df = new SimpleDateFormat ("dd/MM/yyyy", Locale.US);
                for (int j = 1; j < 8; j++) {
                    switch (j) {
                        case 1:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvMonTotal.setText ("" + jsonObject.getInt (AppConfigTags.HOUR));
                            }
                            break;
                        case 2:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvTueTotal.setText ("" + jsonObject.getInt (AppConfigTags.HOUR));
                            }
                            break;
                        case 3:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvWedTotal.setText ("" + jsonObject.getInt (AppConfigTags.HOUR));
                            }
                            break;
                        case 4:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvThurTotal.setText ("" + jsonObject.getInt (AppConfigTags.HOUR));
                            }
                            break;
                        case 5:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvFriTotal.setText ("" + jsonObject.getInt (AppConfigTags.HOUR));
                            }
                            break;
                        case 6:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvSatTotal.setText ("" + jsonObject.getInt (AppConfigTags.HOUR));
                            }
                            break;
                        case 7:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvSunTotal.setText ("" + jsonObject.getInt (AppConfigTags.HOUR));
                            }
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
        return myEmployeeList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvMonTotal;
        TextView tvTueTotal;
        TextView tvWedTotal;
        TextView tvThurTotal;
        TextView tvFriTotal;
        TextView tvSatTotal;
        TextView tvSunTotal;
        
        public ViewHolder (View view) {
            super (view);
            tvName = (TextView) view.findViewById (R.id.tvName);
            tvMonTotal = (TextView) view.findViewById (R.id.tvMonTotal);
            tvTueTotal = (TextView) view.findViewById (R.id.tvTueTotal);
            tvWedTotal = (TextView) view.findViewById (R.id.tvWedTotal);
            tvThurTotal = (TextView) view.findViewById (R.id.tvThurTotal);
            tvFriTotal = (TextView) view.findViewById (R.id.tvFriTotal);
            tvSatTotal = (TextView) view.findViewById (R.id.tvSatTotal);
            tvSunTotal = (TextView) view.findViewById (R.id.tvSunTotal);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
    
    
}


