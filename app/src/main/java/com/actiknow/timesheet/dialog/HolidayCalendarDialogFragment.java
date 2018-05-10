package com.actiknow.timesheet.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.Holiday;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class HolidayCalendarDialogFragment extends DialogFragment {
    ImageView ivCancel;
    TextView tvTitle;
    ProgressDialog progressDialog;
    
    CoordinatorLayout clMain;
    AppDetailsPref appDetailsPref;
    List<Holiday> holidayList = new ArrayList<> ();
    LinearLayout llMain;
    TextView tvDay;
    TextView tvDate;
    TextView tvHoliday;
    
    public static HolidayCalendarDialogFragment newInstance () {
        HolidayCalendarDialogFragment fragment = new HolidayCalendarDialogFragment ();
        Bundle args = new Bundle ();
        fragment.setArguments (args);
        return fragment;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setStyle (DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }
    
    @Override
    public void onActivityCreated (Bundle arg0) {
        super.onActivityCreated (arg0);
        Window window = getDialog ().getWindow ();
        window.getAttributes ().windowAnimations = R.style.DialogAnimation;
//        if (Build.VERSION.SDK_INT >= 21) {
//            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor (ContextCompat.getColor (getActivity (), R.color.text_color_white));
//        }
    }
    
    @Override
    public void onResume () {
        super.onResume ();
        
    }
    
    @Override
    public void onStart () {
        super.onStart ();
        Dialog d = getDialog ();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow ().setLayout (width, height);
        }
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.fragment_dialog_holiday_calendar, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        clMain = (CoordinatorLayout) root.findViewById (R.id.clMain);
        llMain = (LinearLayout) root.findViewById (R.id.llMain);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
//        project_id = bundle.getInt (AppConfigTags.PROJECT_ID);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        progressDialog = new ProgressDialog (getActivity ());
        appDetailsPref = AppDetailsPref.getInstance ();
        
        holidayList.add (new Holiday (1, "Monday", "01/01/2018", "New Year Day"));
        holidayList.add (new Holiday (2, "Friday", "26/01/2018", "Republic Day"));
        holidayList.add (new Holiday (3, "Friday", "02/03/2018", "Holi"));
        holidayList.add (new Holiday (4, "Friday", "30/03/2018", "Good Friday*"));
        holidayList.add (new Holiday (5, "Monday", "30/04/2018", "Buddha Purnima*"));
        holidayList.add (new Holiday (6, "Wednesday", "04/07/2018", "U.S. Independence Day*"));
        holidayList.add (new Holiday (7, "Wednesday", "15/08/2018", "Independence Day"));
        holidayList.add (new Holiday (8, "Tuesday", "02/10/2018", "Gandhi Jayanti"));
        holidayList.add (new Holiday (9, "Friday", "19/10/2018", "Dusssehra"));
        holidayList.add (new Holiday (10, "Wednesday", "07/11/2018", "Diwali"));
        holidayList.add (new Holiday (11, "Thursday", "08/11/2018", "Govardhan Puja*"));
        holidayList.add (new Holiday (12, "Tuesday", "15/12/2018", "Christmas Day"));
        
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
                
            }
        });
        
        
    }
    
    private void setData () {
       /* try {
            JSONArray jsonArray = new JSONArray (appDetailsPref.getStringPref (getActivity (), AppDetailsPref.CLIENTS));
            for (int j = 0; j < jsonArray.length (); j++) {
                JSONObject jsonObject = jsonArray.getJSONObject (j);
                clientList.add (j, new Client (
                        jsonObject.getInt (AppConfigTags.CLIENT_ID),
                        jsonObject.getString (AppConfigTags.CLIENT_NAME)));
            }
            clientAdapter.notifyDataSetChanged ();
        } catch (JSONException e) {
            e.printStackTrace ();
        }*/
        
        
        for (int i = 0; i < holidayList.size (); i++) {
            Holiday holiday = holidayList.get (i);
            LayoutInflater layoutInflater = (LayoutInflater) getActivity ().getBaseContext ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate (R.layout.list_item_holiday, null);
            tvDate = (TextView) addView.findViewById (R.id.tvDate);
            tvDay = (TextView) addView.findViewById (R.id.tvDay);
            tvHoliday = (TextView) addView.findViewById (R.id.tvHoliday);
            
            tvDay.setText (holiday.getDay ());
            tvDate.setText (holiday.getDate ());
            tvHoliday.setText (holiday.getHoliday_name ());
            addView.setId (i);
            llMain.addView (addView);
        }
        
        
    }





   /* private void sendClientsDetailsToServer (final String clientName, final String clientEmail) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (getActivity (), progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.ADD_CLIENT, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.ADD_CLIENT,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        clientList.clear ();
                                        appDetailsPref.putStringPref (getActivity (), AppDetailsPref.CLIENTS, jsonObj.getString (AppConfigTags.CLIENTS));

                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.CLIENTS);
                                        for (int j = 0; j < jsonArray.length (); j++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (j);
                                            if (clientName.equalsIgnoreCase (jsonObject.getString (AppConfigTags.CLIENT_NAME))) {
                                                onDialogResultListener.onPositiveResult (jsonObject.getInt (AppConfigTags.CLIENT_ID), jsonObject.getString (AppConfigTags.CLIENT_NAME));
                                                getDialog ().dismiss ();
                                            }
*//*
                                            clientList.add (j, new Client (
                                                    jsonObject.getInt (AppConfigTags.CLIENT_ID),
                                                    jsonObject.getString (AppConfigTags.CLIENT_NAME)));
*//*
                                        }
//                                        clientAdapter.notifyDataSetChanged ();


                                        //  AddProjectActivity.client_name = clientName;
//                                        getDialog ().dismiss ();

                                    } else {
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.CLIENT_NAME, clientName);
                    params.put (AppConfigTags.CLIENT_EMAIL, clientEmail);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (getActivity (), AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }*/
    
    
}