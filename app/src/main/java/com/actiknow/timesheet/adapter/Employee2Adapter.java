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
import com.actiknow.timesheet.model.Employee2;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class Employee2Adapter extends RecyclerView.Adapter<Employee2Adapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    ProgressDialog progressDialog;
    private Activity activity;
    private List<Employee2> employee2List = new ArrayList<> ();
    private int project_id = 0;
    
    public Employee2Adapter (Activity activity, List<Employee2> employee2List, int project_id) {
        this.activity = activity;
        this.employee2List = employee2List;
        this.project_id = project_id;
        progressDialog = new ProgressDialog (activity);
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_employee2, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, final int position) {
        final Employee2 assignEmployee = employee2List.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvName);
        
        holder.tvName.setText (assignEmployee.getEmployee_name ());
        holder.tvRole.setText (assignEmployee.getRole ());
    
        holder.ivDelete.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                MaterialDialog dialog = new MaterialDialog.Builder (activity)
                        .content ("Do you wish to remove " + assignEmployee.getEmployee_name () + " from this Project?")
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
                                removeEmployeeFromProject (assignEmployee.getEmployee_id (), project_id, position);
                            }
                        }).build ();
                dialog.show ();
            
            }
        });
    }
    
    @Override
    public int getItemCount () {
        return employee2List.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    private void removeEmployeeFromProject (int employee_id, int project_id, final int position) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showProgressDialog (activity, progressDialog, activity.getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.DELETE_PROJECT_OWNER + "project/" + project_id + "/owner/" + employee_id, true);
            StringRequest strRequest = new StringRequest (Request.Method.DELETE, AppConfigURL.DELETE_PROJECT_OWNER + "project/" + project_id + "/owner/" + employee_id,
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
                                        employee2List.remove (position);
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
        TextView tvRole;
        ImageView ivDelete;
        
        public ViewHolder (View view) {
            super (view);
            tvName = (TextView) view.findViewById (R.id.tvName);
            tvRole = (TextView) view.findViewById (R.id.tvRole);
            ivDelete = (ImageView) view.findViewById (R.id.ivDelete);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}