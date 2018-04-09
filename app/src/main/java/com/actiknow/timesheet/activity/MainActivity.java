package com.actiknow.timesheet.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.actiknow.timesheet.utils.SimpleDividerItemDecoration;
import com.actiknow.timesheet.utils.Utils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private AccountHeader headerResult = null;
    private Drawer result = null;
    ImageView ivNavigation;
    Bundle savedInstanceState;
    EditText etNoOfHours;
    EditText etWorkDate;
    EditText etSelectProject;
    AppDetailsPref appDetailsPref;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String date = "", time = "", address2 = "";
    String clients[] = {"Jason Byrne", "Spencer", "Mathias", "EdCandy", "J&J", "Nati", "Bench"};






    ProjectAdapter projectAdapter;
    ArrayList<Project> projectList = new ArrayList<>();
    ProgressDialog progressDialog;
    String allProjects;

    RecyclerView rvProjectList;
    CoordinatorLayout clMain;
    TextView tvTitle;
    FloatingActionButton fabAddProject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initAdapter();
        initListener();
        isLogin();
        initDrawer();
        projectList();

    }



    private void initListener() {



        ivNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.openDrawer();
            }
        });

      /*  etSelectProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Clients")
                        .items(projects)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                etSelectProject.setText(text);
                            }
                        })
                        .show();
            }
        });

        etWorkDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(etWorkDate);
            }
        });
*/


/*

        fabAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddProjectDialogFragment fragment = AddProjectDialogFragment.newInstance(allClients);

                fragment.setDismissListener(new ProjectActivity.MyDialogCloseListener2() {
                    @Override
                    public void handleDialogClose(DialogInterface dialog) {
                        projectList();
                    }
                });
                fragment.show(ft, "test");
            }
        });
*/


    }




        private void isLogin () {
            if (appDetailsPref.getStringPref (MainActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY).length () == 0) {
                Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                startActivity (intent);
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }

    }

    private void initView() {
        ivNavigation = (ImageView) findViewById(R.id.ivNavigation);
       /* etSelectProject = (EditText) findViewById(R.id.etSelectProject);
        etNoOfHours = (EditText) findViewById(R.id.etNoOfHours);
        etWorkDate = (EditText) findViewById(R.id.etWorkDate);
*/

        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        rvProjectList = (RecyclerView) findViewById(R.id.rvProjectList);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        fabAddProject = (FloatingActionButton) findViewById(R.id.fabAddProject);
    }

    private void initData() {
        appDetailsPref=AppDetailsPref.getInstance();

        projectList.clear();
        progressDialog = new ProgressDialog(this);
     /*   projectList.add (new Project (1, "HomeTrust", "This is the timesheet application for actinow employees", "3 hours"));
        projectList.add (new Project (2, "ActiProject", "This is the timesheet application for actinow employees", "3 hours"));
        projectList.add (new Project (3, "P&K", "This is the timesheet application for actinow employees", "3 hours"));*/
        projectAdapter = new ProjectAdapter(this, projectList);
        rvProjectList.setAdapter(projectAdapter);
        rvProjectList.setHasFixedSize(true);
        rvProjectList.addItemDecoration(new SimpleDividerItemDecoration(this));
        rvProjectList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvProjectList.setItemAnimator(new DefaultItemAnimator());
        projectAdapter.SetOnItemClickListener (new ProjectAdapter.OnItemClickListener () {

            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(MainActivity.this, ProjectActivityDetail.class);
                intent.putExtra("allProjects",allProjects);
                intent.putExtra("position",position);
                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initAdapter() {

    }

    private void initDrawer () {
        IProfile profile = new IProfile () {
            @Override
            public Object withName (String name) {
                return null;
            }

            @Override
            public StringHolder getName () {
                return null;
            }

            @Override
            public Object withEmail (String email) {
                return null;
            }

            @Override
            public StringHolder getEmail () {
                return null;
            }

            @Override
            public Object withIcon (Drawable icon) {
                return null;
            }

            @Override
            public Object withIcon (Bitmap bitmap) {
                return null;
            }

            @Override
            public Object withIcon (@DrawableRes int iconRes) {
                return null;
            }

            @Override
            public Object withIcon (String url) {
                return null;
            }

            @Override
            public Object withIcon (Uri uri) {
                return null;
            }

            @Override
            public Object withIcon (IIcon icon) {
                return null;
            }

            @Override
            public ImageHolder getIcon () {
                return null;
            }

            @Override
            public Object withSelectable (boolean selectable) {
                return null;
            }

            @Override
            public boolean isSelectable () {
                return false;
            }

            @Override
            public Object withIdentifier (long identifier) {
                return null;
            }

            @Override
            public long getIdentifier () {
                return 0;
            }
        };

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
                    .withTypeface (SetTypeFace.getTypeface (this))
                    .withPaddingBelowHeader (false)
                    .withSelectionListEnabled (false)
                    .withSelectionListEnabledForSingleProfile (false)
                    .withProfileImagesVisible (true)
                    .withOnlyMainProfileImageVisible (false)
                    .withDividerBelowHeader (true)
                    .withHeaderBackground (R.color.primary)
                    .withSavedInstance (savedInstanceState)
                    .withOnAccountHeaderListener (new AccountHeader.OnAccountHeaderListener () {
                        @Override
                        public boolean onProfileChanged (View view, IProfile profile, boolean currentProfile) {

                            return false;
                        }
                    })
                    .build ();
            headerResult.addProfiles (new ProfileDrawerItem ()
                    .withIcon (R.mipmap.ic_launcher)
                    .withName ("Sudhanshu Sharma")
                    .withEmail ("Sudhanshu.Sharma@actiknow.com"));

        result = new DrawerBuilder ()
                .withActivity (this)
                .withAccountHeader (headerResult)
//                .withToolbar (toolbar)
//                .withItemAnimator (new AlphaCrossFadeAnimator ())
                .addDrawerItems (
                        new PrimaryDrawerItem ().withName ("Home").withIcon (FontAwesome.Icon.faw_home).withIdentifier (1).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Projects").withIcon (FontAwesome.Icon.faw_wordpress).withIdentifier (2).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Clients").withIcon (FontAwesome.Icon.faw_user).withIdentifier (3).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Leave").withIcon (FontAwesome.Icon.faw_leaf).withIdentifier (4).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                )
                .withSavedInstance (savedInstanceState)
                .withOnDrawerItemClickListener (new Drawer.OnDrawerItemClickListener () {
                    @Override
                    public boolean onItemClick (View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier ()) {
                            case 2:
                                Intent intent2 = new Intent(MainActivity.this, ProjectActivity.class);
                                startActivity(intent2);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                break;


                            case 3:
                                Intent intent3 = new Intent(MainActivity.this, ClientsActivity.class);
                                startActivity(intent3);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                break;

                            case 4:
                                Intent intent4 = new Intent(MainActivity.this, LeaveActivity.class);
                                startActivity(intent4);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                break;

                        }
                        return false;
                    }
                })
                .build ();
//        result.getActionBarDrawerToggle ().setDrawerIndicatorEnabled (false);
    }

    private void showLogOutDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .limitIconToDefaultSize()
                .content("Do you wish to Sign Out?")
                .positiveText("Yes")
                .negativeText("No")
                .typeface(SetTypeFace.getTypeface(MainActivity.this), SetTypeFace.getTypeface(MainActivity.this))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                }).build();
        dialog.show();
    }


    private void selectDate(final EditText etPickupDate) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etPickupDate.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year);
                date = etPickupDate.getText().toString().trim();
                Log.e("date", date);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public void projectList() {
        if (NetworkConnection.isNetworkAvailable(MainActivity.this)) {
            projectList.clear();
            Utils.showProgressDialog(MainActivity.this, progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, AppConfigTags.URL, AppConfigURL.PROJECTS, true);
            StringRequest strRequest = new StringRequest(Request.Method.GET, AppConfigURL.PROJECTS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean is_error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!is_error) {
                                        JSONArray jsonArray = jsonObj.getJSONArray(AppConfigTags.PROJECTS);
                                        allProjects = jsonObj.getJSONArray(AppConfigTags.PROJECTS).toString();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            Project project = new Project(
                                                    jsonObject.getInt(AppConfigTags.PROJECT_ID),
                                                    jsonObject.getString(AppConfigTags.PROJECT_TITLE),
                                                    jsonObject.getString(AppConfigTags.PROJECT_DESCRIPTION),
                                                    jsonObject.getString(AppConfigTags.PROJECT_ALLOTED_HOUR)


                                            );
                                            projectList.add(i, project);
                                        }


                                        projectAdapter.notifyDataSetChanged();
                                    } else {
                                        Utils.showSnackBar(MainActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);

                                }
                            } else {
                                Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog(Log.ERROR, AppConfigTags.ERROR, new String(response.data), true);
                            }
                            Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    AppDetailsPref appDetailsPref = AppDetailsPref.getInstance();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put(AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref(MainActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest, 5);
        } else {
            Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }

    public interface MyDialogCloseListener2 {
        public void handleDialogClose(DialogInterface dialog);
    }



}
