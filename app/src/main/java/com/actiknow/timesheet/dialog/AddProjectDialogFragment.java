package com.actiknow.timesheet.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.EventLogTags;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.activity.ProjectActivity;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppConfigURL;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Constants;
import com.actiknow.timesheet.utils.NetworkConnection;
import com.actiknow.timesheet.utils.Utils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//created by rahul jain 12/04/2018
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
//created by rahul jain 12/04/2018
public class AddProjectDialogFragment extends DialogFragment {
    ImageView ivCancel;
    EditText etStartDate;
    EditText etEndDate;
    EditText etClientName;
    EditText etProjectName;
    EditText etProjectBudget;
    EditText etProjectHourCost;
    EditText etProjectAllottedHour;
    EditText etProjectDescription;
    private int mYear, mMonth, mDay;
    String start_date = "";
    String end_date = "";
    String clients;
    int clientId;
    ArrayList<String> clientList = new ArrayList<>();
    ArrayList<Integer> clientID = new ArrayList<>();
    ProgressDialog progressDialog;
    CoordinatorLayout clMain;
    AppDetailsPref appDetailsPref;
    TextView tvSubmit;
    ProjectActivity.MyDialogCloseListener2 myDialogCloseListener2;

    public static AddProjectDialogFragment newInstance(String allClients) {
        AddProjectDialogFragment f = new AddProjectDialogFragment();
        Bundle args = new Bundle();
        args.putString("allClients", allClients);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        Window window = getDialog().getWindow();
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.text_color_white));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialog_add_project, container, false);
        initView(root);
        initBundle();
        initData();
        initListener();
        setData();
        return root;
    }

    private void initView(View root) {
        ivCancel = (ImageView) root.findViewById(R.id.ivCancel);
        etStartDate = (EditText) root.findViewById(R.id.etStartDate);
        etEndDate = (EditText) root.findViewById(R.id.etEndDate);
        etClientName = (EditText) root.findViewById(R.id.etClientName);
        etProjectName = (EditText) root.findViewById(R.id.etProjectName);
        etProjectBudget = (EditText) root.findViewById(R.id.etProjectBudget);
        etProjectHourCost = (EditText) root.findViewById(R.id.etProjectHourCost);
        etProjectAllottedHour = (EditText) root.findViewById(R.id.etProjectAllottedHour);
        etProjectDescription = (EditText) root.findViewById(R.id.etProjectDescription);
        clMain=(CoordinatorLayout)root.findViewById(R.id.clMain);
        tvSubmit=(TextView)root.findViewById(R.id.tvSubmit);
    }

    private void initBundle() {
        Bundle bundle = this.getArguments();
        clients = bundle.getString("allClients");
    }

    private void initData() {
        Utils.setTypefaceToAllViews (getActivity(), clMain);
        progressDialog=new ProgressDialog(getActivity());
        appDetailsPref=AppDetailsPref.getInstance();

    }

    private void initListener() {
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        etClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title("Clients")
                        .items(clientList)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                etClientName.setText(text);
                                clientId=clientID.get(which);
                                Log.e("item number", "" + clientID.get(which));
                            }
                        })
                        .show();
            }
        });

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(etStartDate, 1);
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(etEndDate,2);
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendProjectDetailsToServer(etProjectName.getText().toString(),clientId,etProjectBudget.getText().toString(),
                        etProjectHourCost.getText().toString(),etProjectAllottedHour.getText().toString(),etProjectDescription.getText().toString(),
                        start_date,end_date);
            }
        });


    }

    private void selectDate(final EditText etPickupDate, final int i) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(i == 1){
                    start_date = year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);
                }else if(i == 2) {
                    end_date = year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);
                }
                etPickupDate.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year);

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void setData() {
        try {
            JSONArray jsonArray = new JSONArray(clients);
            if (jsonArray.length() > 0) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    clientList.add(jsonObject.getString(AppConfigTags.CLIENT_NAME));
                    clientID.add(jsonObject.getInt(AppConfigTags.CLIENT_ID));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    private void sendProjectDetailsToServer(final String projectName, final int clientId, final String projectBudget, final String hourCost, final String allottedHour, final String projectDescription, final String startDate, final String endDate) {
        if (NetworkConnection.isNetworkAvailable (getActivity())) {
            Utils.showProgressDialog (getActivity(), progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.ADD_PROJECT, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.ADD_PROJECT,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        getDialog ().dismiss ();

                                    } else {
                                        Utils.showSnackBar (getActivity(), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                   // String sDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
                  //  String eDate = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
                    Map<String, String> params = new Hashtable<String, String>();

                    params.put (AppConfigTags.PROJECT_CLIENT_ID, String.valueOf(clientId));
                    params.put (AppConfigTags.PROJECT_TITLE, projectName);
                    params.put (AppConfigTags.PROJECT_BUDGET, projectBudget);
                    params.put (AppConfigTags.PROJECT_HOUR_COST, hourCost);
                    params.put (AppConfigTags.PROJECT_ALLOTED_HOUR, allottedHour);
                    params.put (AppConfigTags.PROJECT_DESCRIPTION, projectDescription);
                    params.put (AppConfigTags.PROJECT_STARTED_AT, startDate);
                    params.put (AppConfigTags.PROJECT_COMPLETE_AT, endDate);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (getActivity(), AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }

    public void setDismissListener (ProjectActivity.MyDialogCloseListener2 addProjectDialogFragment) {
        this.myDialogCloseListener2 = addProjectDialogFragment;
    }

    @Override
    public void onDismiss (DialogInterface dialog) {
        super.onDismiss (dialog);
        if (myDialogCloseListener2 != null) {
            myDialogCloseListener2.handleDialogClose (null);
        }
    }


}