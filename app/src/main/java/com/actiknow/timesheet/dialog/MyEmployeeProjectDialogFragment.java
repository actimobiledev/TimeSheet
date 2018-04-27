package com.actiknow.timesheet.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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
import com.actiknow.timesheet.adapter.MyEmployeeProjectAdapter;
import com.actiknow.timesheet.model.MyEmployeeProject;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyEmployeeProjectDialogFragment extends DialogFragment {
    OnDialogResultListener onDialogResultListener;
    String projects_json = "";
    
    RecyclerView rvProjects;
    List<MyEmployeeProject> projectList = new ArrayList<> ();
    List<MyEmployeeProject> projectListTemp = new ArrayList<> ();
    
    LinearLayoutManager linearLayoutManager;
    MyEmployeeProjectAdapter myEmployeeProjectAdapter;
    
    ImageView ivCancel;
    ImageView ivSearch;
    TextView tvTitle;
    RelativeLayout rlSearch;
    EditText etSearch;
    
    
    RelativeLayout rlNoResultFound;
    
    AppDetailsPref appDetailsPref;
    
    public static MyEmployeeProjectDialogFragment newInstance (String projects_json) {
        MyEmployeeProjectDialogFragment fragment = new MyEmployeeProjectDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.PROJECTS, projects_json);
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
        View root = inflater.inflate (R.layout.fragment_dialog_my_employee_projects, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rvProjects = (RecyclerView) root.findViewById (R.id.rvProjects);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        ivSearch = (ImageView) root.findViewById (R.id.ivSearch);
        etSearch = (EditText) root.findViewById (R.id.etSearch);
        rlSearch = (RelativeLayout) root.findViewById (R.id.rlSearch);
        rlNoResultFound = (RelativeLayout) root.findViewById (R.id.rlNoResultFound);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        projects_json = bundle.getString (AppConfigTags.PROJECTS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        appDetailsPref = AppDetailsPref.getInstance ();
        
        linearLayoutManager = new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false);
        
        myEmployeeProjectAdapter = new MyEmployeeProjectAdapter (getActivity (), projectList);
        myEmployeeProjectAdapter.SetOnItemClickListener (new MyEmployeeProjectAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
            }
        });
        
        rvProjects.setAdapter (myEmployeeProjectAdapter);
        rvProjects.setHasFixedSize (true);
        rvProjects.setLayoutManager (linearLayoutManager);
        rvProjects.setItemAnimator (new DefaultItemAnimator ());
//        rvProjects.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        
        rvProjects.addItemDecoration (
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
                    MyEmployeeProjectAdapter myEmployeeProjectAdapter = new MyEmployeeProjectAdapter (getActivity (), projectList);
                    rvProjects.setAdapter (myEmployeeProjectAdapter);
                    
                    myEmployeeProjectAdapter.SetOnItemClickListener (new MyEmployeeProjectAdapter.OnItemClickListener () {
                        @Override
                        public void onItemClick (View view, int position) {
                        }
                    });
                    
                    rlNoResultFound.setVisibility (View.GONE);
                    rvProjects.setVisibility (View.VISIBLE);
                }
                if (s.toString ().length () > 0) {
                    projectListTemp.clear ();
                    for (MyEmployeeProject project : projectList) {
                        if (project.getProject_title ().toUpperCase ().contains (s.toString ().toUpperCase ()) ||
                                project.getProject_title ().toLowerCase ().contains (s.toString ().toLowerCase ())) {
                            projectListTemp.add (project);
                        }
                    }
                    myEmployeeProjectAdapter = new MyEmployeeProjectAdapter (getActivity (), projectListTemp);
                    rvProjects.setAdapter (myEmployeeProjectAdapter);
                    myEmployeeProjectAdapter.SetOnItemClickListener (new MyEmployeeProjectAdapter.OnItemClickListener () {
                        @Override
                        public void onItemClick (View view, int position) {
                        }
                    });
                    
                    if (projectListTemp.size () == 0) {
                        rlNoResultFound.setVisibility (View.VISIBLE);
                        rvProjects.setVisibility (View.GONE);
                    } else {
                        rvProjects.setVisibility (View.VISIBLE);
                        rlNoResultFound.setVisibility (View.GONE);
                    }
                }
            }
        });
    }
    
    private void setData () {
        try {
            JSONArray jsonArray = new JSONArray (projects_json);
            for (int i = 0; i < jsonArray.length (); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject (i);
                MyEmployeeProject project = new MyEmployeeProject (
                        jsonObject.getInt (AppConfigTags.PROJECT_ID),
                        jsonObject.getString (AppConfigTags.PROJECT_TITLE),
                        jsonObject.getJSONArray (AppConfigTags.HOURS).toString ()
                );
                projectList.add (i, project);
            }
            
            myEmployeeProjectAdapter.notifyDataSetChanged ();
            
            if (jsonArray.length () > 0) {
                rlNoResultFound.setVisibility (View.GONE);
            } else {
                rlNoResultFound.setVisibility (View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace ();
            Utils.showSnackBar (getActivity (), rlNoResultFound, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
        public abstract void onPositiveResult ();
        
        public abstract void onNegativeResult ();
    }
}