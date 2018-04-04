package com.actiknow.timesheet.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.actiknow.timesheet.R;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Calendar;

public class AddProjectDialogFragment extends DialogFragment {
    ImageView ivCancel;
    EditText etStartDate;
    EditText etEndDate;
    EditText etClientName;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String date = "", time = "", address2 = "";
    String clients[] = {"Jason Byrne", "Spencer", "Mathias", "EdCandy", "J&J", "Nati"};
    
    public static AddProjectDialogFragment newInstance(){
        AddProjectDialogFragment f = new AddProjectDialogFragment();
        return f;
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
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (getActivity (), R.color.text_color_white));
        }
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
        View root = inflater.inflate (R.layout.fragment_dialog_add_project, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        ivCancel = (ImageView) root.findViewById(R.id.ivCancel);
        etStartDate = (EditText) root.findViewById (R.id.etStartDate);
        etEndDate = (EditText) root.findViewById (R.id.etEndDate);
        etClientName = (EditText) root.findViewById (R.id.etClientName);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
    }
    
    private void initData () {

    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });

        etClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title("Clients")
                        .items(clients)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                etClientName.setText(text);
                            }
                        })
                        .show();
            }
        });

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(etStartDate);
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(etEndDate);
            }
        });
    }

    private void selectDate(final EditText etPickupDate) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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