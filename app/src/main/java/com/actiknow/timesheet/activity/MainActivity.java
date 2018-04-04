package com.actiknow.timesheet.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.utils.SetTypeFace;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private AccountHeader headerResult = null;
    private Drawer result = null;
    ImageView ivNavigation;
    Bundle savedInstanceState;
    EditText etNoOfHours;
    EditText etWorkDate;
    EditText etSelectProject;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String date = "", time = "", address2 = "";
    String clients[] = {"Jason Byrne", "Spencer", "Mathias", "EdCandy", "J&J", "Nati", "Bench"};

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

    }

    private void initListener() {

        ivNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.openDrawer();
            }
        });

        etSelectProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Clients")
                        .items(clients)
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

    }


    private void isLogin() {

    }

    private void initView() {
        ivNavigation = (ImageView) findViewById(R.id.ivNavigation);
        etSelectProject = (EditText) findViewById(R.id.etSelectProject);
        etNoOfHours = (EditText) findViewById(R.id.etNoOfHours);
        etWorkDate = (EditText) findViewById(R.id.etWorkDate);
    }

    private void initData() {

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
                        new PrimaryDrawerItem ().withName ("Projects").withIcon (FontAwesome.Icon.faw_home).withIdentifier (2).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Leave").withIcon (FontAwesome.Icon.faw_sign_out).withIdentifier (3).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                )
                .withSavedInstance (savedInstanceState)
                .withOnDrawerItemClickListener (new Drawer.OnDrawerItemClickListener () {
                    @Override
                    public boolean onItemClick (View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier ()) {
                            case 2:
                                Intent intent = new Intent(MainActivity.this, ProjectActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                break;

                            case 3:
                                Intent intent2 = new Intent(MainActivity.this, LeaveActivity.class);
                                startActivity(intent2);
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


}
