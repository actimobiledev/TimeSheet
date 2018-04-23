package com.actiknow.timesheet.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class MyProfileActivity extends AppCompatActivity {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static int PERMISSION_REQUEST_CODE = 11;
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    AppDetailsPref appDetailsPref;
    RelativeLayout rlBack;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBar;
    ImageView ivProfileImage;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_my_profile_2);
//        initView ();
//        initData ();
//        initListener ();
//        checkPermissions ();
    }
    
    private void initData () {
        progressDialog = new ProgressDialog (MyProfileActivity.this);
        Utils.setTypefaceToAllViews (this, clMain);
        appDetailsPref = AppDetailsPref.getInstance ();
        collapsingToolbarLayout.setTitleEnabled (true);
        
    }
    
    public void checkPermissions () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission (Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    (checkSelfPermission (Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                    checkSelfPermission (Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, MyProfileActivity.PERMISSION_REQUEST_CODE);
            }
        }
    }
    
    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale (permission);
                    if (! showRationale) {
                        AlertDialog.Builder builder = new AlertDialog.Builder (MyProfileActivity.this);
                        builder.setMessage ("Permission are required please enable them on the App Setting page")
                                .setCancelable (false)
                                .setPositiveButton ("OK", new DialogInterface.OnClickListener () {
                                    public void onClick (DialogInterface dialog, int id) {
                                        dialog.dismiss ();
                                        Intent intent = new Intent (Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts ("package", getPackageName (), null));
                                        startActivity (intent);
                                    }
                                });
                        AlertDialog alert = builder.create ();
                        alert.show ();
                    } else if (Manifest.permission.ACCESS_FINE_LOCATION.equals (permission)) {
                    } else if (Manifest.permission.ACCESS_COARSE_LOCATION.equals (permission)) {
                    } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals (permission)) {
                    }
                }
            }
        }
        
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        appBar = (AppBarLayout) findViewById (R.id.appBar);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById (R.id.collapsing_toolbar);
        ivProfileImage = (ImageView) findViewById (R.id.ivProfileImage);
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        
        ivProfileImage.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                selectImage ();
            }
        });
    }
    
    private void selectImage () {
        final CharSequence[] options = {"From Camera", "From Gallery"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder (MyProfileActivity.this);
        builder.setItems (options, new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialog, int item) {
                if (options[item].equals ("From Camera")) {
                    Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File (Environment.getExternalStorageDirectory (), "temp.jpg");
                    intent.putExtra (MediaStore.EXTRA_OUTPUT, Uri.fromFile (f));
                    startActivityForResult (intent, 1);
                } else if (options[item].equals ("From Gallery")) {
                    Intent intent = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult (intent, 2);
                }
            }
        });
        builder.show ();
    }
    
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File (Environment.getExternalStorageDirectory ().toString ());
                for (File temp : f.listFiles ()) {
                    if (temp.getName ().equals ("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options ();
                    bitmap = Utils.compressBitmap (BitmapFactory.decodeFile (f.getAbsolutePath (), bitmapOptions), MyProfileActivity.this);
                    
                    
                    ivProfileImage.setImageBitmap (bitmap);
                    
                    
                    Log.e ("bitmap", "" + bitmap);
                    String path = Environment
                            .getExternalStorageDirectory ()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete ();
                    OutputStream outFile = null;
                    File file = new File (path, String.valueOf (System.currentTimeMillis ()) + ".jpg");
                    try {
                        outFile = new FileOutputStream (file);
                        bitmap.compress (Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush ();
                        outFile.close ();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace ();
                    } catch (IOException e) {
                        e.printStackTrace ();
                    } catch (Exception e) {
                        e.printStackTrace ();
                    }
                } catch (Exception e) {
                    e.printStackTrace ();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData ();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver ().query (selectedImage, filePath, null, null, null);
                c.moveToFirst ();
                int columnIndex = c.getColumnIndex (filePath[0]);
                String picturePath = c.getString (columnIndex);
                c.close ();
                Bitmap thumbnail = Utils.compressBitmap (BitmapFactory.decodeFile (picturePath), MyProfileActivity.this);
                
                Log.e ("thumbnil", "" + thumbnail);
                ivProfileImage.setImageBitmap (thumbnail);
                
            }
        }
    }
    

        /*appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed
                    tvTitleName.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setScrimAnimationDuration((long) 400);
                    collapsingToolbarLayout.setScrimsShown(true);
//                    collapsingToolbarLayout.setScrimVisibleHeightTrigger ((int) Utils.dpFromPx (PropertyDetailActivity.this, 60));
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.text_color_white));
                } else if (verticalOffset == 0) {
                    // Expanded
                    tvTitleName.setVisibility(View.GONE);
                    collapsingToolbarLayout.setScrimsShown(false);

                    if (Build.VERSION.SDK_INT >= 21) {
                        Window window = getWindow();
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.TRANSPARENT);
                    } else {
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    }

                    collapsingToolbarLayout.setContentScrim(null);
                    collapsingToolbarLayout.setStatusBarScrim(null);
                } else {
                    // transparent statusbar for marshmallow and above
                    tvTitleName.setVisibility(View.VISIBLE);
                    tvTitleName.setTextColor(getResources().getColor(R.color.text_color_black));
                    collapsingToolbarLayout.setScrimsShown(false);
                    if (Build.VERSION.SDK_INT >= 21) {
                        Window window = getWindow();
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.TRANSPARENT);
                    } else {
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    }
                    collapsingToolbarLayout.setContentScrim(null);
                    collapsingToolbarLayout.setStatusBarScrim(null);
                }

            }
        });

    }
*/

    
   /* private void sendPasswordChangeToServer (final String oldPassword, final String newPassword) {
        if (NetworkConnection.isNetworkAvailable (MyProfileActivity.this)) {
            Utils.showProgressDialog(MyProfileActivity.this, progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.CHANGE_PASSWORD, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.CHANGE_PASSWORD,
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
                                        new MaterialDialog.Builder (MyProfileActivity.this)
                                                .content (message)
                                                .positiveText ("OK")
                                                .typeface (SetTypeFace.getTypeface (MyProfileActivity.this), SetTypeFace.getTypeface (MyProfileActivity.this))
                                                .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                    @Override
                                                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        dialog.dismiss ();
                                                        finish ();
                                                    }
                                                })
                                                .show ();
                                    } else {
                                        Utils.showSnackBar (MyProfileActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (MyProfileActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (MyProfileActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (MyProfileActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put (AppConfigTags.OLD_PASSWORD, oldPassword);
                    params.put (AppConfigTags.NEW_PASSWORD, newPassword);
                    
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put(AppConfigTags.HEADER_EMPLOYEE_LOGIN_KEY, appDetailsPref.getStringPref(MyProfileActivity.this, AppDetailsPref.EMPLOYEE_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }*/
}