package com.actiknow.timesheet.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.MyEmployee;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppConfigURL;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Constants;
import com.actiknow.timesheet.utils.NetworkConnection;
import com.actiknow.timesheet.utils.SetTypeFace;
import com.actiknow.timesheet.utils.Utils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MyEmployeeAdapter extends RecyclerView.Adapter<MyEmployeeAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    ProgressDialog progressDialog;
    private Activity activity;
    private List<MyEmployee> myEmployeeList = new ArrayList<> ();
    
    public MyEmployeeAdapter (Activity activity, List<MyEmployee> myEmployeeList) {
        this.activity = activity;
        this.myEmployeeList = myEmployeeList;
        progressDialog = new ProgressDialog (activity);
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_my_employee, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, final int position) {
        final MyEmployee myEmployee = myEmployeeList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvName);
        holder.tvName.setText (myEmployee.getEmployee_name ());
        String start_date = "", end_date = "";
    
        try {
            JSONArray jsonArray = new JSONArray (myEmployee.getTotal_json ());
            for (int i = 0; i < jsonArray.length (); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject (i);
                Calendar c = Calendar.getInstance (Locale.GERMAN);
                c.set (Calendar.DAY_OF_WEEK, c.MONDAY);
                DateFormat df = new SimpleDateFormat ("yyyy-MM-dd", Locale.US);
                DateFormat df2 = new SimpleDateFormat ("dd/MM/yy", Locale.US);
                DateFormat df3 = new SimpleDateFormat ("dd/MM", Locale.US);
                for (int j = 1; j < 8; j++) {
                    switch (j) {
                        case 1:
                            if (df.format (c.getTime ()).equalsIgnoreCase (jsonObject.getString (AppConfigTags.DATE))) {
                                holder.tvMonTotal.setText ("" + jsonObject.getString (AppConfigTags.HOUR));
                            }
                            start_date = df2.format (c.getTime ());
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
                            end_date = df2.format (c.getTime ());
                            holder.tvSunDate.setText (df3.format (c.getTime ()));
                            break;
                    }
                    c.add (Calendar.DATE, 1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    
    
        holder.tvDateRange.setText ("(" + start_date + " - " + end_date + ")");
    
    
        holder.ivDelete.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                MaterialDialog dialog = new MaterialDialog.Builder (activity)
                        .content ("Do you wish to remove " + myEmployee.getEmployee_name () + " from your list?")
                        .positiveColor (activity.getResources ().getColor (R.color.primary_text))
                        .contentColor (activity.getResources ().getColor (R.color.primary_text))
                        .negativeColor (activity.getResources ().getColor (R.color.primary_text))
                        .typeface (SetTypeFace.getTypeface (activity), SetTypeFace.getTypeface (activity))
                        .canceledOnTouchOutside (true)
                        .cancelable (true)
                        .positiveText (R.string.dialog_action_yes)
                        .negativeText (R.string.dialog_action_no)
                        .onPositive (new MaterialDialog.SingleButtonCallback () {
                            @Override
                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                deleteMyEmployee (myEmployee.getEmployee_id (), position);
                            }
                        }).build ();
                dialog.show ();
            
            }
        });
    }
    
    @Override
    public int getItemCount () {
        return myEmployeeList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    private void deleteMyEmployee (int employee_id, final int position) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showProgressDialog (activity, progressDialog, activity.getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.DELETE_MY_EMPLOYEES + "/" + employee_id, true);
            StringRequest strRequest = new StringRequest (Request.Method.DELETE, AppConfigURL.DELETE_MY_EMPLOYEES + "/" + employee_id,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        myEmployeeList.remove (position);
                                        notifyDataSetChanged ();
                                    } else {
                                        Utils.showToast (activity, message, false);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                        }
                    })
            
            {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<> ();
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    AppDetailsPref appDetailsPref = AppDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (activity, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            strRequest.setRetryPolicy (new
                    DefaultRetryPolicy (DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Utils.sendRequest (strRequest, 30);
        } else {
        }
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
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
        ImageView ivDelete;
        TextView tvDateRange;
        
        public ViewHolder (View view) {
            super (view);
            tvName = (TextView) view.findViewById (R.id.tvName);
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
            ivDelete = (ImageView) view.findViewById (R.id.ivDelete);
            tvDateRange = (TextView) view.findViewById (R.id.tvDateRange);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}


