package com.actiknow.timesheet.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.actiknow.timesheet.R;
import com.actiknow.timesheet.activity.AddProjectActivity;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppConfigURL;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Constants;
import com.actiknow.timesheet.utils.NetworkConnection;
import com.actiknow.timesheet.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
//created by rahul jain 12/04/2018
import java.util.Map;

public class AddClientDialogFragment extends DialogFragment {
    ImageView ivCancel;
    private EditText etClientName;
    private EditText etEmailAddress;
    private EditText etContactNumber;
    private EditText etSkypeAddress;
    private EditText etClientSource;
    private EditText etLocation;
    private EditText etCompanyName;
    private EditText etStatus;
    private TextView tvSubmit;
    private ProgressDialog progressDialog;
    AppDetailsPref appDetailsPref;
    private CoordinatorLayout clMain;
    AddProjectActivity.MyDialogCloseListener addClientDialogFragment;

    public static AddClientDialogFragment newInstance() {
        AddClientDialogFragment f = new AddClientDialogFragment();
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
        View root = inflater.inflate(R.layout.fragment_dialog_add_client, container, false);
        initView(root);
        initBundle();
        initData();
        initListener();
        return root;
    }

    private void initView(View root) {
        ivCancel = (ImageView) root.findViewById(R.id.ivCancel);
        etClientName = (EditText) root.findViewById(R.id.etClientName);
        etEmailAddress = (EditText) root.findViewById(R.id.etEmailAddress);
        etContactNumber = (EditText) root.findViewById(R.id.etContactNumber);
        etSkypeAddress = (EditText) root.findViewById(R.id.etSkypeAddress);
        etClientSource = (EditText) root.findViewById(R.id.etClientSource);
        etLocation = (EditText) root.findViewById(R.id.etLocation);
        etCompanyName = (EditText) root.findViewById(R.id.etCompanyName);
        etStatus = (EditText) root.findViewById(R.id.etStatus);
        tvSubmit = (TextView) root.findViewById(R.id.tvSubmit);
        clMain = (CoordinatorLayout) root.findViewById(R.id.clMain);
    }

    private void initBundle() {
        Bundle bundle = this.getArguments();
    }

    private void initData() {
        Utils.setTypefaceToAllViews (getActivity(), clMain);
        appDetailsPref = AppDetailsPref.getInstance();
        progressDialog = new ProgressDialog(getActivity());

    }

    private void initListener() {
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendClientsDetailsToServer(etClientName.getText().toString().trim(), etEmailAddress.getText().toString().trim()
                        , etContactNumber.getText().toString().trim(), etSkypeAddress.getText().toString().trim(), etClientSource.getText().toString().trim(),
                        etLocation.getText().toString().trim(), etCompanyName.getText().toString().trim(), etStatus.getText().toString().trim());
            }
        });

    }
    private void sendClientsDetailsToServer(final String clientName, final String clientEmail, final String clientNumber, final String clientSkype, final String clientSource, final String clientLocation, final String clientCompany, final String clientStatus) {
        if (NetworkConnection.isNetworkAvailable(getActivity())) {
            Utils.showProgressDialog(getActivity(), progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.ADD_CLIENT, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.ADD_CLIENT,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
                                        appDetailsPref.putStringPref(getActivity(), AppDetailsPref.CLIENTS, jsonObj.getString(AppConfigTags.CLIENTS));
                                        AddProjectActivity.client_name = clientName;
                                        getDialog().dismiss();

                                    } else {
                                        Utils.showSnackBar(getActivity(), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog(Log.ERROR, AppConfigTags.ERROR, new String(response.data), true);
                            }
                            Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.CLIENT_NAME, clientName);
                    params.put(AppConfigTags.CLIENT_EMAIL, clientEmail);
                    params.put(AppConfigTags.CLIENT_CONTACT, clientNumber);
                    params.put(AppConfigTags.CLIENT_SKYPE, clientSkype);
                    params.put(AppConfigTags.CLIENT_SOURCE, clientSource);
                    params.put(AppConfigTags.CLIENT_LOCATION, clientLocation);
                    params.put(AppConfigTags.CLIENT_COMAPNY, clientCompany);
                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put(AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref(getActivity(), AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest1, 60);
        } else {
            Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }

    public void setDismissListener(AddProjectActivity.MyDialogCloseListener addClientDialogFragment) {
        this.addClientDialogFragment = addClientDialogFragment;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (addClientDialogFragment != null) {
            addClientDialogFragment.handleDialogClose(null);
        }
    }

}