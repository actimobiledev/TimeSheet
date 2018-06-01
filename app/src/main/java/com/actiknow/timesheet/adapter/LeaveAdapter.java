package com.actiknow.timesheet.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.Leave;
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
import com.github.marlonlom.utilities.timeago.TimeAgo;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    ProgressDialog progressDialog;
    
    private Activity activity;
    private Dialog dialog;
    private List<Leave> leaveList = new ArrayList<> ();
    
    public LeaveAdapter (Activity activity, Dialog dialog, List<Leave> leaveList) {
        this.activity = activity;
        this.dialog = dialog;
        this.leaveList = leaveList;
        progressDialog = new ProgressDialog (activity);
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
    
        if (leave.getEmployee_name ().length () > 0) {
            holder.llButtons.setVisibility (View.VISIBLE);
            holder.tvEmployeeName.setVisibility (View.VISIBLE);
            holder.tvEmployeeName.setText ("Employee : " + leave.getEmployee_name ());
            holder.ivCancel.setVisibility (View.GONE);
        
        } else {
            holder.tvEmployeeName.setVisibility (View.GONE);
            holder.llButtons.setVisibility (View.GONE);
            holder.ivCancel.setVisibility (View.VISIBLE);
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
                    holder.tvRemark.setText ("Remark : " + leave.getRemark ());
                } else {
                    holder.tvRemark.setVisibility (View.GONE);
                }
                break;
            case 2:
                holder.tvStatus.setText ("Declined");
                holder.tvUpdatedBy.setText (leave.getUpdated_by ());
                if (leave.getRemark ().length () > 0) {
                    holder.tvRemark.setVisibility (View.VISIBLE);
                    holder.tvRemark.setText ("Remark : " + leave.getRemark ());
                } else {
                    holder.tvRemark.setVisibility (View.GONE);
                }
                break;
            case 3:
                holder.ivCancel.setVisibility (View.GONE);
                holder.tvStatus.setText ("Cancelled");
                holder.tvUpdatedBy.setText ("N/A");
                break;
        }
        holder.ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                MaterialDialog dialog = new MaterialDialog.Builder (activity)
                        .content ("Do you wish to cancel the Leave Request?")
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
                                cancelLeave (leave.getRequest_id (), holder.tvStatus, holder.ivCancel);
                            }
                        }).build ();
                dialog.show ();
            }
        });
    
        holder.tvApprove.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                final MaterialDialog.Builder mBuilder = new MaterialDialog.Builder (activity)
                        .content ("Would you like to add a remark?")
                        .contentColor (activity.getResources ().getColor (R.color.primary_text))
                        .positiveColor (activity.getResources ().getColor (R.color.primary_text))
                        .negativeColor (activity.getResources ().getColor (R.color.primary_text))
                        .typeface (SetTypeFace.getTypeface (activity), SetTypeFace.getTypeface (activity))
                        .inputRangeRes (0, 255, R.color.text_color_red)
                        .alwaysCallInputCallback ()
                        .canceledOnTouchOutside (true)
                        .cancelable (true)
                        .positiveText (R.string.dialog_action_submit)
                        .negativeText (activity.getResources ().getString (R.string.dialog_action_cancel));
            
            
                mBuilder.input (activity.getResources ().getString (R.string.dialog_hint_optional), null, new MaterialDialog.InputCallback () {
                    @Override
                    public void onInput (MaterialDialog dialog, CharSequence input) {
                        if (input.toString ().length () == 0) {
                            mBuilder.positiveText (R.string.dialog_action_submit);
                        } else {
                            mBuilder.positiveText (R.string.dialog_action_submit);
                        }
                    }
                });
            
                mBuilder.onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        updateLeave (leave.getRequest_id (), leave.getEmployee_id (), 1, dialog.getInputEditText ().getText ().toString ().trim ());
                    }
                });
                MaterialDialog dialog = mBuilder.build ();
                dialog.show ();
            }
        });
    
        holder.tvDecline.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                final MaterialDialog.Builder mBuilder = new MaterialDialog.Builder (activity)
                        .content ("Would you like to add a remark?")
                        .contentColor (activity.getResources ().getColor (R.color.primary_text))
                        .positiveColor (activity.getResources ().getColor (R.color.primary_text))
                        .negativeColor (activity.getResources ().getColor (R.color.primary_text))
                        .typeface (SetTypeFace.getTypeface (activity), SetTypeFace.getTypeface (activity))
                        .inputRangeRes (0, 255, R.color.text_color_red)
                        .alwaysCallInputCallback ()
                        .canceledOnTouchOutside (true)
                        .cancelable (true)
                        .positiveText (R.string.dialog_action_submit)
                        .negativeText (activity.getResources ().getString (R.string.dialog_action_cancel));
            
            
                mBuilder.input (activity.getResources ().getString (R.string.dialog_hint_optional), null, new MaterialDialog.InputCallback () {
                    @Override
                    public void onInput (MaterialDialog dialog, CharSequence input) {
                        if (input.toString ().length () == 0) {
                            mBuilder.positiveText (R.string.dialog_action_submit);
                        } else {
                            mBuilder.positiveText (R.string.dialog_action_submit);
                        }
                    }
                });
            
                mBuilder.onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        updateLeave (leave.getRequest_id (), leave.getEmployee_id (), 2, dialog.getInputEditText ().getText ().toString ().trim ());
                    }
                });
                MaterialDialog dialog = mBuilder.build ();
                dialog.show ();
            }
        });
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
    
    private void cancelLeave (final int request_id, final TextView tvStatus, final ImageView ivCancel) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showProgressDialog (activity, progressDialog, activity.getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_CANCEL_LEAVE, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_CANCEL_LEAVE,
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
                                        ivCancel.setVisibility (View.GONE);
                                        tvStatus.setText ("Cancelled");
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
                    params.put (AppConfigTags.REQUEST_ID, String.valueOf (request_id));
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
    
    private void updateLeave (final int request_id, final int employee_id, final int status, final String remark) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showProgressDialog (activity, progressDialog, activity.getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_UPDATE_LEAVE, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_UPDATE_LEAVE,
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
                                        dialog.dismiss ();
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
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<> ();
                    params.put (AppConfigTags.REQUEST_ID, String.valueOf (request_id));
                    params.put (AppConfigTags.EMPLOYEE_ID, String.valueOf (employee_id));
                    params.put (AppConfigTags.STATUS, String.valueOf (status));
                    params.put (AppConfigTags.REMARK, remark);
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
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvEmployeeName;
        TextView tvType;
        TextView tvDates;
        TextView tvDescription;
        TextView tvAppliedAt;
        TextView tvStatus;
        TextView tvUpdatedBy;
        TextView tvRemark;
        ImageView ivCancel;
        LinearLayout llButtons;
        TextView tvApprove;
        TextView tvDecline;
        
        public ViewHolder (View view) {
            super (view);
            tvEmployeeName = (TextView) view.findViewById (R.id.tvEmployeeName);
            tvType = (TextView) view.findViewById (R.id.tvType);
            tvAppliedAt = (TextView) view.findViewById (R.id.tvAppliedAt);
            tvDates = (TextView) view.findViewById (R.id.tvLeaveDates);
            tvDescription = (TextView) view.findViewById (R.id.tvDescription);
            tvStatus = (TextView) view.findViewById (R.id.tvStatus);
            tvUpdatedBy = (TextView) view.findViewById (R.id.tvUpdatedBy);
            tvRemark = (TextView) view.findViewById (R.id.tvRemark);
            ivCancel = (ImageView) view.findViewById (R.id.ivCancel);
            llButtons = (LinearLayout) view.findViewById (R.id.llButtons);
            tvApprove = (TextView) view.findViewById (R.id.tvApprove);
            tvDecline = (TextView) view.findViewById (R.id.tvDecline);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}


