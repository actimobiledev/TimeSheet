package com.actiknow.timesheet.activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.adapter.ClientsAdapter;
import com.actiknow.timesheet.adapter.ProjectAdapter;
import com.actiknow.timesheet.dialog.AddClientDialogFragment;
import com.actiknow.timesheet.dialog.AddProjectDialogFragment;
import com.actiknow.timesheet.model.Clients;
import com.actiknow.timesheet.model.Project;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppConfigURL;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Constants;
import com.actiknow.timesheet.utils.NetworkConnection;
import com.actiknow.timesheet.utils.SimpleDividerItemDecoration;
import com.actiknow.timesheet.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Rahul jain on 05/04/2018.
 */

public class ClientsActivity extends AppCompatActivity {
    Bundle savedInstanceState;
    RecyclerView rvClientList;
    CoordinatorLayout clMain;
    TextView tvTitle;
    FloatingActionButton fabAddClient;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout rlBack;
    ClientsAdapter clientAdapter;
    ArrayList<Clients> clientList = new ArrayList<>();
    ProgressDialog progressDialog;

    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_clients);
        this.savedInstanceState = savedInstanceState;
        initView ();
        initData ();
        initListener ();

    }

    @Override
    public void onResume () {
        super.onResume ();
        ClientsList ();
        // put your code here...

    }
    
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        rvClientList = (RecyclerView) findViewById (R.id.rvClientList);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        fabAddClient = (FloatingActionButton) findViewById(R.id.fabAddClients);
    }
    
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        fabAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.FragmentManager fm = getFragmentManager();
                android.app.FragmentTransaction ft = fm.beginTransaction();
                AddClientDialogFragment fragment = new AddClientDialogFragment().newInstance();
                fragment.setDismissListener(new MyDialogCloseListener() {
                    @Override
                    public void handleDialogClose(DialogInterface dialog) {

                        ClientsList ();
                    }
                });
                fragment.show(ft, "test");
            }
        });
    }



    
    private void initData () {

        progressDialog = new ProgressDialog (this);


       /* clientList.clear ();
        clientList.add (new Clients (1, "Spencer", "client Description 1", "3 hours"));
        clientList.add (new Clients (2, "Jason", "client Description 2", "3 hours"));
        clientList.add (new Clients (3, "Jaypee", "client Description 3", "3 hours"));
        clientList.add (new Clients (4, "P&K", "client Description 4", "3 hours"));
        clientList.add (new Clients (5, "AKRL", "client Description 5", "3 hours"));
        clientList.add (new Clients (6, "Nati", "clients Description 6", "3 hours"));*/
        clientAdapter = new ClientsAdapter(this, clientList);
        rvClientList.setAdapter (clientAdapter);
        rvClientList.setHasFixedSize (true);
        rvClientList.addItemDecoration (new SimpleDividerItemDecoration(this));
        rvClientList.setLayoutManager (new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvClientList.setItemAnimator (new DefaultItemAnimator());
    }
    public void ClientsList () {
        if (NetworkConnection.isNetworkAvailable (ClientsActivity.this)) {
            clientList.clear ();
            Utils.showProgressDialog (ClientsActivity.this, progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.CLIENTS, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.CLIENTS,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        //clientList.clear ();
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.CLIENTS);
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (i);
                                            Clients client = new Clients (jsonObject.getInt(AppConfigTags.CLIENT_ID),
                                                    jsonObject.getInt(AppConfigTags.CLIENT_EMP_ID),
                                                    jsonObject.getString(AppConfigTags.CLIENT_NAME),
                                                    jsonObject.getString(AppConfigTags.CLIENT_EMAIL),
                                                    jsonObject.getString(AppConfigTags.CLIENT_CONTACT),
                                                    jsonObject.getString(AppConfigTags.CLIENT_SKYPE),
                                                    jsonObject.getString(AppConfigTags.CLIENT_SOURCE),
                                                    jsonObject.getString(AppConfigTags.CLIENT_LOCATION),
                                                    jsonObject.getString(AppConfigTags.CLIENT_COMAPNY));
                                            clientList.add (i, client);
                                        }
                                        clientAdapter.notifyDataSetChanged ();
                                    } else {
                                    Utils.showSnackBar (ClientsActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                }
                                progressDialog.dismiss ();
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showSnackBar (ClientsActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    
                                }
                            } else {
                                Utils.showSnackBar (ClientsActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showSnackBar (ClientsActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    AppDetailsPref appDetailsPref= AppDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (ClientsActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
            Utils.showSnackBar (ClientsActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }


    public interface MyDialogCloseListener {
        public void handleDialogClose (DialogInterface dialog);
    }
}
