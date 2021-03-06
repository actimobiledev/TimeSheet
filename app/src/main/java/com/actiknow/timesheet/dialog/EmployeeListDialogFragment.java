package com.actiknow.timesheet.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.actiknow.timesheet.adapter.EmployeeAdapter;
import com.actiknow.timesheet.model.Employee;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListDialogFragment extends DialogFragment {
    OnDialogResultListener onDialogResultListener;
    
    RecyclerView rvEmployees;
    List<Employee> employeeList = new ArrayList<> ();
    List<Employee> employeeListTemp = new ArrayList<> ();
    
    
    LinearLayoutManager linearLayoutManager;
    EmployeeAdapter employeeAdapter;
    
    ImageView ivCancel;
    ImageView ivSearch;
    TextView tvTitle;
    RelativeLayout rlSearch;
    EditText etSearch;
    
    RelativeLayout rlNoResultFound;
    
    String employees;
    ProgressDialog progressDialog;
    
    AppDetailsPref appDetailsPref;
    
    public static EmployeeListDialogFragment newInstance () {
        EmployeeListDialogFragment fragment = new EmployeeListDialogFragment ();
        Bundle args = new Bundle ();
//        args.putInt (AppConfigTags.PROJECT_ID, project_id);
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
            public boolean onKey (android.content.DialogInterface dialog, int keyCode, android.view.KeyEvent event) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
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
        View root = inflater.inflate (R.layout.fragment_dialog_employee_list, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rvEmployees = (RecyclerView) root.findViewById (R.id.rvEmployees);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        ivSearch = (ImageView) root.findViewById (R.id.ivSearch);
        etSearch = (EditText) root.findViewById (R.id.etSearch);
        rlSearch = (RelativeLayout) root.findViewById (R.id.rlSearch);
        rlNoResultFound = (RelativeLayout) root.findViewById (R.id.rlNoResultFound);
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
        
        employeeAdapter = new EmployeeAdapter (getActivity (), employeeList);
        employeeAdapter.SetOnItemClickListener (new EmployeeAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                Utils.hideSoftKeyboard (getActivity ());
                onDialogResultListener.onPositiveResult (employeeList.get (position).getId (), employeeList.get (position).getName ());
                getDialog ().dismiss ();
            }
        });
        
        rvEmployees.setAdapter (employeeAdapter);
        rvEmployees.setHasFixedSize (true);
        rvEmployees.setLayoutManager (linearLayoutManager);
        rvEmployees.setItemAnimator (new DefaultItemAnimator ());
//        rvEmployees.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    
        rvEmployees.addItemDecoration (
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
                    EmployeeAdapter employeeAdapter = new EmployeeAdapter (getActivity (), employeeList);
                    rvEmployees.setAdapter (employeeAdapter);
                
                    employeeAdapter.SetOnItemClickListener (new EmployeeAdapter.OnItemClickListener () {
                        @Override
                        public void onItemClick (View view, int position) {
                            Utils.hideSoftKeyboard (getActivity ());
                            onDialogResultListener.onPositiveResult (employeeList.get (position).getId (), employeeList.get (position).getName ());
                            getDialog ().dismiss ();
                        }
                    });
                
                    rlNoResultFound.setVisibility (View.GONE);
                    rvEmployees.setVisibility (View.VISIBLE);
                }
                if (s.toString ().length () > 0) {
                    employeeListTemp.clear ();
                    for (Employee employee : employeeList) {
                        if (employee.getName ().toUpperCase ().contains (s.toString ().toUpperCase ()) ||
                                employee.getName ().toLowerCase ().contains (s.toString ().toLowerCase ())) {
                            employeeListTemp.add (employee);
                        }
                    }
                    employeeAdapter = new EmployeeAdapter (getActivity (), employeeListTemp);
                    rvEmployees.setAdapter (employeeAdapter);
                    employeeAdapter.SetOnItemClickListener (new EmployeeAdapter.OnItemClickListener () {
                        @Override
                        public void onItemClick (View view, int position) {
                            Utils.hideSoftKeyboard (getActivity ());
                            onDialogResultListener.onPositiveResult (employeeListTemp.get (position).getId (), employeeListTemp.get (position).getName ());
                            getDialog ().dismiss ();
                        }
                    });
                
                    if (employeeListTemp.size () == 0) {
                        rlNoResultFound.setVisibility (View.VISIBLE);
                        rvEmployees.setVisibility (View.GONE);
                    } else {
                        rvEmployees.setVisibility (View.VISIBLE);
                        rlNoResultFound.setVisibility (View.GONE);
                    }
                }
            }
        });
    }
    
    private void setData () {
        try {
            JSONArray jsonArray = new JSONArray (appDetailsPref.getStringPref (getActivity (), AppDetailsPref.EMPLOYEES));
            for (int j = 0; j < jsonArray.length (); j++) {
                JSONObject jsonObject = jsonArray.getJSONObject (j);
                employeeList.add (j, new Employee (
                        jsonObject.getInt (AppConfigTags.EMPLOYEE_ID),
                        jsonObject.getString (AppConfigTags.EMPLOYEE_NAME),
                        jsonObject.getString (AppConfigTags.EMPLOYEE_WORK_EMAIL)));
            }
            employeeAdapter.notifyDataSetChanged ();
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
    
    public interface OnDialogResultListener {
        public abstract void onPositiveResult (int employee_id, String employee_name);
        
        public abstract void onNegativeResult ();
    }
    
}