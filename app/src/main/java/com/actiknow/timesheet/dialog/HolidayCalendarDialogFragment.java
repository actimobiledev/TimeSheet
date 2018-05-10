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
        holidayList.add (new Holiday (12, "Tuesday", "25/12/2018", "Christmas Day"));
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
    
            Utils.setTypefaceToAllViews (getActivity (), tvDate);
            tvDay.setText (holiday.getDay ());
            tvDate.setText (holiday.getDate ());
            tvHoliday.setText (holiday.getHoliday_name ());
            addView.setId (i);
            llMain.addView (addView);
        }
    }
}