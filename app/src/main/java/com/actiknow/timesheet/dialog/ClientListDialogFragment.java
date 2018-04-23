package com.actiknow.timesheet.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.adapter.ClientAdapter;
import com.actiknow.timesheet.model.Client;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppConfigURL;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Constants;
import com.actiknow.timesheet.utils.NetworkConnection;
import com.actiknow.timesheet.utils.Utils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ClientListDialogFragment extends DialogFragment {
    OnDialogResultListener onDialogResultListener;
    
    RecyclerView rvClients;
    List<Client> clientList = new ArrayList<> ();
    List<Client> clientListTemp = new ArrayList<> ();
    
    LinearLayoutManager linearLayoutManager;
    ClientAdapter clientAdapter;
    
    ImageView ivCancel;
    ImageView ivSearch;
    TextView tvTitle;
    RelativeLayout rlSearch;
    EditText etSearch;
    
    RelativeLayout rlNoResultFound;
    ProgressDialog progressDialog;
    
    FloatingActionButton fabAddClient;
    CoordinatorLayout clMain;
    
    AppDetailsPref appDetailsPref;
    
    
    public static ClientListDialogFragment newInstance () {
        ClientListDialogFragment fragment = new ClientListDialogFragment ();
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
        getDialog ().setOnKeyListener (new DialogInterface.OnKeyListener () {
            @Override
            public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction () != KeyEvent.ACTION_UP)
                        return true;
                    else {
                        if (rlSearch.getVisibility () == View.VISIBLE) {
                            final Handler handler = new Handler ();
                            handler.postDelayed (new Runnable () {
                                @Override
                                public void run () {
                                    ivSearch.setVisibility (View.VISIBLE);
                                    etSearch.setText ("");
                                }
                            }, 300);
                            final Handler handler2 = new Handler ();
                            handler2.postDelayed (new Runnable () {
                                @Override
                                public void run () {
                                    final InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow (getView ().getWindowToken (), 0);
                                }
                            }, 600);
                            rlSearch.setVisibility (View.GONE);
                        } else {
                            getDialog ().dismiss ();
                        }
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
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
        View root = inflater.inflate (R.layout.fragment_dialog_client_list, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rvClients = (RecyclerView) root.findViewById (R.id.rvClients);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        ivSearch = (ImageView) root.findViewById (R.id.ivSearch);
        etSearch = (EditText) root.findViewById (R.id.etSearch);
        rlSearch = (RelativeLayout) root.findViewById (R.id.rlSearch);
        rlNoResultFound = (RelativeLayout) root.findViewById (R.id.rlNoResultFound);
        fabAddClient = (FloatingActionButton) root.findViewById (R.id.fabAddClient);
        clMain = (CoordinatorLayout) root.findViewById (R.id.clMain);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
//        project_id = bundle.getInt (AppConfigTags.PROJECT_ID);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        progressDialog = new ProgressDialog (getActivity ());
        appDetailsPref = AppDetailsPref.getInstance ();
        
        linearLayoutManager = new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false);
        
        clientAdapter = new ClientAdapter (getActivity (), clientList);
        clientAdapter.SetOnItemClickListener (new ClientAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                Utils.hideSoftKeyboard (getActivity ());
                onDialogResultListener.onPositiveResult (clientList.get (position).getId (), clientList.get (position).getName ());
                getDialog ().dismiss ();
            }
        });
        
        rvClients.setAdapter (clientAdapter);
        rvClients.setHasFixedSize (true);
        rvClients.setLayoutManager (linearLayoutManager);
        rvClients.setItemAnimator (new DefaultItemAnimator ());
//        rvClients.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        rvClients.addItemDecoration (
                new DividerItemDecoration (getActivity (), linearLayoutManager.getOrientation ()) {
                    @Override
                    public void getItemOffsets (Rect outRect, View view, RecyclerView
                            parent, RecyclerView.State state) {
                        int position = parent.getChildAdapterPosition (view);
                        // hide the divider for the last child
                        if (position == parent.getAdapter ().getItemCount () - 1) {
                            outRect.setEmpty ();
                        } else {
                            super.getItemOffsets (outRect, view, parent, state);
                        }
                    }
                }
        );
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (rlSearch.getVisibility () == View.VISIBLE) {
                    final Handler handler = new Handler ();
                    handler.postDelayed (new Runnable () {
                        @Override
                        public void run () {
                            ivSearch.setVisibility (View.VISIBLE);
                            etSearch.setText ("");
                        }
                    }, 600);
                    final Handler handler2 = new Handler ();
                    handler2.postDelayed (new Runnable () {
                        @Override
                        public void run () {
                            final InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow (getView ().getWindowToken (), 0);
                        }
                    }, 300);
                    rlSearch.setVisibility (View.GONE);
                } else {
                    getDialog ().dismiss ();
                }
            }
        });
        ivSearch.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                final Handler handler = new Handler ();
                handler.postDelayed (new Runnable () {
                    @Override
                    public void run () {
                        ivSearch.setVisibility (View.GONE);
                        etSearch.requestFocus ();
                    }
                }, 300);
                final Handler handler2 = new Handler ();
                handler2.postDelayed (new Runnable () {
                    @Override
                    public void run () {
                        final InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }, 600);
                rlSearch.setVisibility (View.VISIBLE);
            }
        });
        
        
        etSearch.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
                if (s.toString ().length () == 0) {
                    ClientAdapter clientAdapter = new ClientAdapter (getActivity (), clientList);
                    rvClients.setAdapter (clientAdapter);
                    
                    clientAdapter.SetOnItemClickListener (new ClientAdapter.OnItemClickListener () {
                        @Override
                        public void onItemClick (View view, int position) {
                            Utils.hideSoftKeyboard (getActivity ());
                            onDialogResultListener.onPositiveResult (clientList.get (position).getId (), clientList.get (position).getName ());
                            getDialog ().dismiss ();
                        }
                    });
                    
                    rlNoResultFound.setVisibility (View.GONE);
                    rvClients.setVisibility (View.VISIBLE);
                }
                if (s.toString ().length () > 0) {
                    clientListTemp.clear ();
                    for (Client client : clientList) {
                        if (client.getName ().toUpperCase ().contains (s.toString ().toUpperCase ()) ||
                                client.getName ().toLowerCase ().contains (s.toString ().toLowerCase ())) {
                            clientListTemp.add (client);
                        }
                    }
                    clientAdapter = new ClientAdapter (getActivity (), clientListTemp);
                    rvClients.setAdapter (clientAdapter);
                    clientAdapter.SetOnItemClickListener (new ClientAdapter.OnItemClickListener () {
                        @Override
                        public void onItemClick (View view, int position) {
                            Utils.hideSoftKeyboard (getActivity ());
                            onDialogResultListener.onPositiveResult (clientListTemp.get (position).getId (), clientListTemp.get (position).getName ());
                            getDialog ().dismiss ();
                        }
                    });
                    
                    if (clientListTemp.size () == 0) {
                        rlNoResultFound.setVisibility (View.VISIBLE);
                        rvClients.setVisibility (View.GONE);
                    } else {
                        rvClients.setVisibility (View.VISIBLE);
                        rlNoResultFound.setVisibility (View.GONE);
                    }
                }
            }
        });
    
    
        fabAddClient.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                final MaterialDialog.Builder builder = new MaterialDialog.Builder (getActivity ())
                        .customView (R.layout.dialog_add_client, true)
                        .positiveText (R.string.dialog_action_save)
                        .negativeText (R.string.dialog_action_cancel);
                final MaterialDialog dialog = builder.build ();
            
                final EditText etClientName = dialog.getCustomView ().findViewById (R.id.etClientName);
                final EditText etClientEmail = dialog.getCustomView ().findViewById (R.id.etClientEmail);
                final TextView tvCounterName = dialog.getCustomView ().findViewById (R.id.tvCounterName);
                Utils.setTypefaceToAllViews (getActivity (), etClientEmail);
            
            
                dialog.getActionButton (DialogAction.POSITIVE).setEnabled (false);
            
                etClientEmail.addTextChangedListener (new TextWatcher () {
                    @Override
                    public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {
                        if (etClientEmail.getText ().toString ().length () > 0) {
                            etClientEmail.setError (null);
                        }
                    }
                
                    @Override
                    public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.toString ().length () != 0 && etClientName.getText ().toString ().length () != 0) {
                            dialog.getActionButton (DialogAction.POSITIVE).setEnabled (true);
                        } else {
                            dialog.getActionButton (DialogAction.POSITIVE).setEnabled (false);
                        }
                    }
                
                    @Override
                    public void afterTextChanged (Editable editable) {
                    }
                });
            
            
                etClientName.addTextChangedListener (new TextWatcher () {
                    @Override
                    public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {
                        int len = charSequence.toString ().trim ().length ();
                        tvCounterName.setText (len + "/80");
                        if (len > 80) {
                            dialog.getActionButton (DialogAction.POSITIVE).setEnabled (false);
                            tvCounterName.setTextColor (getResources ().getColor (R.color.md_red_900));
                        } else {
                            dialog.getActionButton (DialogAction.POSITIVE).setEnabled (true);
                            tvCounterName.setTextColor (getResources ().getColor (R.color.secondary_text));
                        }
                    
                        if (charSequence.toString ().length () > 0) {
                            etClientName.setError (null);
                        }
                    }
                
                    @Override
                    public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.toString ().length () != 0 && etClientEmail.getText ().toString ().length () != 0) {
                            dialog.getActionButton (DialogAction.POSITIVE).setEnabled (true);
                        } else {
                            dialog.getActionButton (DialogAction.POSITIVE).setEnabled (false);
                        }
                    }
                
                    @Override
                    public void afterTextChanged (Editable editable) {
                    
                    }
                });
            
            
                builder.onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Utils.hideSoftKeyboard (getActivity ());
                        sendClientsDetailsToServer (etClientName.getText ().toString (), etClientEmail.getText ().toString ());
                    }
                });
                dialog.show ();
            }
        
        });
    }
    
    private void setData () {
        try {
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
        }
    }
    
    public void setOnDialogResultListener (OnDialogResultListener listener) {
        this.onDialogResultListener = listener;
    }
    
    @Override
    public void onDismiss (DialogInterface dialog) {
        super.onDismiss (dialog);
        Utils.hideSoftKeyboard (getActivity ());
        if (onDialogResultListener != null) {
            onDialogResultListener.onNegativeResult ();
            dialog.cancel ();
        }
    }
    
    private void sendClientsDetailsToServer (final String clientName, final String clientEmail) {
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
/*
                                            clientList.add (j, new Client (
                                                    jsonObject.getInt (AppConfigTags.CLIENT_ID),
                                                    jsonObject.getString (AppConfigTags.CLIENT_NAME)));
*/
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
    }
    
    public interface OnDialogResultListener {
        public abstract void onPositiveResult (int client_id, String client_name);
        
        public abstract void onNegativeResult ();
    }
}