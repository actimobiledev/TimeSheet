package com.actiknow.timesheet.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.adapter.ProjectAdapter;
import com.actiknow.timesheet.model.Project;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Bundle savedInstanceState;
    AppDetailsPref appDetailsPref;
    RelativeLayout rlNoResultFound;
    ProjectAdapter projectAdapter;
    ArrayList<Project> projectList = new ArrayList<> ();
    ProgressDialog progressDialog;
    String projects_json;
    RecyclerView rvProjectList;
    CoordinatorLayout clMain;
    TextView tvTitle;
    RelativeLayout rlBack;
    SwipeRefreshLayout swipeRefreshLayout;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        initView ();
        initData ();
        initAdapter ();
        initListener ();
        initApplication ();
        isLogin ();
        initDrawer ();
    }
    
    private void isLogin () {
        if (appDetailsPref.getStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY).length () == 0) {
            Intent intent = new Intent (MainActivity.this, LoginActivity.class);
            startActivity (intent);
            finish ();
            overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                result.openDrawer ();
            }
        });
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh () {
                getProjectList ();
            }
        });
        projectAdapter.SetOnItemClickListener (new ProjectAdapter.OnItemClickListener () {
            
            @Override
            public void onItemClick (View view, int position) {
                Project project = projectList.get (position);
                Intent intent = new Intent (MainActivity.this, TimeSheetActivity.class);
                intent.putExtra (AppConfigTags.PROJECTS, projects_json);
                intent.putExtra (AppConfigTags.PROJECT_ID, project.getId ());
                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity (intent);
            }
        });
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        rvProjectList = (RecyclerView) findViewById (R.id.rvProjectList);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        rlNoResultFound = (RelativeLayout) findViewById (R.id.rlNoResultFound);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swipe_refresh_layout);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, clMain);
        swipeRefreshLayout.setRefreshing (true);
        appDetailsPref = AppDetailsPref.getInstance ();
        
        progressDialog = new ProgressDialog (this);
        projectAdapter = new ProjectAdapter (this, projectList);
    }
    
    @Override
    protected void onResume () {
        swipeRefreshLayout.setRefreshing (true);
        super.onResume ();
        getProjectList ();
    }
    
    private void initAdapter () {
        rvProjectList.setAdapter (projectAdapter);
        rvProjectList.setHasFixedSize (true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false);
        rvProjectList.setLayoutManager (linearLayoutManager);
//        rvProjectList.addItemDecoration (new RecyclerViewMargin (
//                (int) Utils.pxFromDp (this, 16),
//                (int) Utils.pxFromDp (this, 16),
//                (int) Utils.pxFromDp (this, 16),
//                (int) Utils.pxFromDp (this, 16),
//                1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        rvProjectList.addItemDecoration (
                new DividerItemDecoration (this, linearLayoutManager.getOrientation ()) {
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
    
    private void initDrawer () {
        DrawerImageLoader.init (new AbstractDrawerImageLoader () {
            @Override
            public void set (ImageView imageView, Uri uri, Drawable placeholder) {
                if (uri != null) {
                    Glide.with (imageView.getContext ()).load (uri).placeholder (placeholder).into (imageView);
                }
            }
            
            @Override
            public void cancel (ImageView imageView) {
                Glide.clear (imageView);
            }
            
            @Override
            public Drawable placeholder (Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name ().equals (tag)) {
                    return DrawerUIUtils.getPlaceHolder (ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name ().equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (com.mikepenz.materialdrawer.R.color.colorPrimary).sizeDp (56);
                } else if ("customUrlItem".equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (R.color.md_white_1000);
                }
                
                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()
                
                return super.placeholder (ctx, tag);
            }
        });
        
        headerResult = new AccountHeaderBuilder ()
                .withActivity (this)
                .withCompactStyle (false)
                .withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                .withPaddingBelowHeader (false)
                .withSelectionListEnabled (false)
                .withSelectionListEnabledForSingleProfile (false)
                .withProfileImagesVisible (true)
                .withDividerBelowHeader (true)
                .withTextColor (getResources ().getColor (R.color.primary_text))
                .withOnlyMainProfileImageVisible (false)
                .withDividerBelowHeader (true)
                .withHeaderBackground (R.color.text_color_grey_light2)
                .withSavedInstance (savedInstanceState)
                .withOnAccountHeaderListener (new AccountHeader.OnAccountHeaderListener () {
                    @Override
                    public boolean onProfileChanged (View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build ();
    
        ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem ();
        profileDrawerItem.withName (appDetailsPref.getStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_NAME));
        profileDrawerItem.withEmail (appDetailsPref.getStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_WORK_EMAIL));
    
    
        if (appDetailsPref.getStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_IMAGE).length () > 0) {
            profileDrawerItem.withIcon (appDetailsPref.getStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_IMAGE));
        } else {
            if (appDetailsPref.getStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_GENDER).equalsIgnoreCase ("F")) {
                profileDrawerItem.withIcon (R.drawable.ic_profile_female);
            } else {
                profileDrawerItem.withIcon (R.drawable.ic_profile_male);
            }
        }
        headerResult.addProfiles (profileDrawerItem);
    
        //case 1: employee
        //case 2: admin
        //case 3: project manager
        
        DrawerBuilder drawerBuilder = new DrawerBuilder ()
                .withActivity (this)
                .withAccountHeader (headerResult)
                .withSavedInstance (savedInstanceState)
                .withOnDrawerItemClickListener (new Drawer.OnDrawerItemClickListener () {
                    @Override
                    public boolean onItemClick (View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier ()) {
                            case 2:
                                Intent intent2 = new Intent (MainActivity.this, MyProjectActivity.class);
                                startActivity (intent2);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 3:
                                Intent intent4 = new Intent (MainActivity.this, LeaveActivity.class);
                                startActivity (intent4);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 5:
                                Intent intent7 = new Intent (MainActivity.this, FeedbackActivity.class);
                                startActivity (intent7);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 6:
                                Intent intent6 = new Intent (MainActivity.this, ChangePasswordActivity.class);
                                startActivity (intent6);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 7:
                                showLogOutDialog ();
                                break;
                        }
                        return false;
                    }
                });
        
        switch (appDetailsPref.getIntPref (MainActivity.this, AppDetailsPref.EMPLOYEE_TYPE)) {
            case 1:
                drawerBuilder.addDrawerItems (
                        new PrimaryDrawerItem ().withName ("Home").withIcon (FontAwesome.Icon.faw_home).withIdentifier (1).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Feedback").withIcon (FontAwesome.Icon.faw_star).withIdentifier (5).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Change Password").withIcon (FontAwesome.Icon.faw_asterisk).withIdentifier (6).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Sign Out").withIcon (FontAwesome.Icon.faw_sign_out).withIdentifier (7).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                );
                result = drawerBuilder.build ();
            case 2:
                drawerBuilder.addDrawerItems (
                        new PrimaryDrawerItem ().withName ("Home").withIcon (FontAwesome.Icon.faw_home).withIdentifier (1).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("My Projects").withIcon (FontAwesome.Icon.faw_wordpress).withIdentifier (2).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Feedback").withIcon (FontAwesome.Icon.faw_star).withIdentifier (5).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Change Password").withIcon (FontAwesome.Icon.faw_asterisk).withIdentifier (6).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Sign Out").withIcon (FontAwesome.Icon.faw_sign_out).withIdentifier (7).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                );
                result = drawerBuilder.build ();
                break;
            case 3:
                drawerBuilder.addDrawerItems (
                        new PrimaryDrawerItem ().withName ("Home").withIcon (FontAwesome.Icon.faw_home).withIdentifier (1).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("My Projects").withIcon (FontAwesome.Icon.faw_wordpress).withIdentifier (2).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Feedback").withIcon (FontAwesome.Icon.faw_star).withIdentifier (5).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Change Password").withIcon (FontAwesome.Icon.faw_asterisk).withIdentifier (6).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Sign Out").withIcon (FontAwesome.Icon.faw_sign_out).withIdentifier (7).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                );
                result = drawerBuilder.build ();
                break;
        }
    }
    
    private void showLogOutDialog () {
        MaterialDialog dialog = new MaterialDialog.Builder (this)
                .contentColor (getResources ().getColor (R.color.primary_text))
                .positiveColor (getResources ().getColor (R.color.primary_text))
                .negativeColor (getResources ().getColor (R.color.primary_text))
                .content ("Do you wish to Sign Out?")
                .positiveText ("Yes")
                .negativeText ("No")
                .typeface (SetTypeFace.getTypeface (MainActivity.this), SetTypeFace.getTypeface (MainActivity.this))
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_NAME, "");
                        appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_MOBILE, "");
                        appDetailsPref.putIntPref (MainActivity.this, AppDetailsPref.EMPLOYEE_TYPE, 0);
                        appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_WORK_EMAIL, "");
                        appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_IMAGE, "");
                        appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY, "");
                        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
    }
    
    public void getProjectList () {
        if (NetworkConnection.isNetworkAvailable (MainActivity.this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.HOME, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.HOME,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    projectList.clear ();
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.PROJECTS);
                                        projects_json = jsonObj.getJSONArray (AppConfigTags.PROJECTS).toString ();
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (i);
                                            Project project = new Project (
                                                    jsonObject.getInt (AppConfigTags.PROJECT_ID),
                                                    jsonObject.getString (AppConfigTags.PROJECT_TITLE),
                                                    jsonObject.getString (AppConfigTags.CLIENT_NAME),
                                                    jsonObject.getString (AppConfigTags.PROJECT_CREATED_BY),
                                                    jsonObject.getString (AppConfigTags.PROJECT_HOURS)
                                            );
                                            projectList.add (i, project);
                                        }
        
                                        if (jsonArray.length () > 0) {
                                            rlNoResultFound.setVisibility (View.GONE);
                                        } else {
                                            rlNoResultFound.setVisibility (View.VISIBLE);
                                        }
        
        
                                        projectAdapter.notifyDataSetChanged ();
                                    } else {
                                        Utils.showSnackBar (MainActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                }
                            } else {
                                Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            swipeRefreshLayout.setRefreshing (false);
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            swipeRefreshLayout.setRefreshing (false);
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    AppDetailsPref appDetailsPref = AppDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 30);
        } else {
            swipeRefreshLayout.setRefreshing (false);
            Utils.showSnackBar (MainActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    private void initApplication () {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager ().getPackageInfo (getPackageName (), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
        }
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_INIT, true);
            final PackageInfo finalPInfo = pInfo;
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_INIT,
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
    
                                        boolean flag = false;
                                        for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                                            if (jsonObj.getString (AppConfigTags.EMPLOYEE_IMAGE).endsWith (ext)) {
                                                flag = true;
                                                break;
                                            }
                                        }
    
                                        appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_NAME, jsonObj.getString (AppConfigTags.EMPLOYEE_NAME));
                                        appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_MOBILE, jsonObj.getString (AppConfigTags.EMPLOYEE_MOBILE));
                                        appDetailsPref.putIntPref (MainActivity.this, AppDetailsPref.EMPLOYEE_TYPE, jsonObj.getInt (AppConfigTags.EMPLOYEE_TYPE));
                                        appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_WORK_EMAIL, jsonObj.getString (AppConfigTags.EMPLOYEE_WORK_EMAIL));
                                        appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_GENDER, jsonObj.getString (AppConfigTags.EMPLOYEE_GENDER));
    
                                        if (flag) {
                                            appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_IMAGE, jsonObj.getString (AppConfigTags.EMPLOYEE_IMAGE));
                                        } else {
                                            appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_IMAGE, "");
                                        }
                                        
                                        appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.CLIENTS, jsonObj.getJSONArray (AppConfigTags.CLIENTS).toString ());
                                        appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.EMPLOYEES, jsonObj.getJSONArray (AppConfigTags.EMPLOYEES).toString ());
                                        appDetailsPref.putStringPref (MainActivity.this, AppDetailsPref.ROLES, jsonObj.getJSONArray (AppConfigTags.ROLES).toString ());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
    
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                        }
                    }) {
    
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<> ();
        
                    params.put (AppConfigTags.APP_VERSION, String.valueOf (finalPInfo.versionCode));
                    params.put (AppConfigTags.DEVICE, "ANDROID");
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
    
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            strRequest.setRetryPolicy (new DefaultRetryPolicy (DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Utils.sendRequest (strRequest, 30);
        } else {
        }
    }
}