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
import com.actiknow.timesheet.adapter.LeaveAdapter;
import com.actiknow.timesheet.model.Leave;
import com.actiknow.timesheet.utils.AppConfigTags;
import com.actiknow.timesheet.utils.AppDetailsPref;
import com.actiknow.timesheet.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeavesListDialogFragment extends DialogFragment {
    OnDialogResultListener onDialogResultListener;
    String leaves_json = "";
    int type_id = 0;
    
    RecyclerView rvLeaves;
    List<Leave> leaveList = new ArrayList<> ();
    List<Leave> leaveListTemp = new ArrayList<> ();
    
    LinearLayoutManager linearLayoutManager;
    LeaveAdapter leaveAdapter;
    
    ImageView ivCancel;
    ImageView ivSearch;
    TextView tvTitle;
    RelativeLayout rlSearch;
    EditText etSearch;
    
    
    RelativeLayout rlNoResultFound;
    ProgressDialog progressDialog;
    
    AppDetailsPref appDetailsPref;
    
    public static LeavesListDialogFragment newInstance (String leaves_json, int type_id) {
        LeavesListDialogFragment fragment = new LeavesListDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.LEAVES, leaves_json);
        args.putInt (AppConfigTags.TYPE_ID, type_id);
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
        View root = inflater.inflate (R.layout.fragment_dialog_leave_list, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rvLeaves = (RecyclerView) root.findViewById (R.id.rvLeaves);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        ivSearch = (ImageView) root.findViewById (R.id.ivSearch);
        etSearch = (EditText) root.findViewById (R.id.etSearch);
        rlSearch = (RelativeLayout) root.findViewById (R.id.rlSearch);
        rlNoResultFound = (RelativeLayout) root.findViewById (R.id.rlNoResultFound);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        leaves_json = bundle.getString (AppConfigTags.LEAVES);
        type_id = bundle.getInt (AppConfigTags.TYPE_ID);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        progressDialog = new ProgressDialog (getActivity ());
        appDetailsPref = AppDetailsPref.getInstance ();
        
        linearLayoutManager = new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false);
        try {
            JSONArray jsonArray = new JSONArray (leaves_json);
            for (int i = 0; i < jsonArray.length (); i++) {
                if (type_id == 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject (i);
                    leaveList.add (i, new Leave (
                            jsonObject.getInt (AppConfigTags.REQUEST_ID),
                            jsonObject.getInt (AppConfigTags.TYPE_ID),
                            jsonObject.getInt (AppConfigTags.LEAVE_STATUS),
                            jsonObject.getDouble (AppConfigTags.LEAVE_AVAILED),
                            jsonObject.getString (AppConfigTags.LEAVE_TYPE),
                            jsonObject.getString (AppConfigTags.LEAVE_FROM),
                            jsonObject.getString (AppConfigTags.LEAVE_TILL),
                            jsonObject.getString (AppConfigTags.LEAVE_DESCRIPTION),
                            jsonObject.getString (AppConfigTags.LEAVE_CREATED_AT),
                            jsonObject.getString (AppConfigTags.LEAVE_UPDATED_AT),
                            jsonObject.getString (AppConfigTags.LEAVE_UPDATED_BY),
                            jsonObject.getString (AppConfigTags.LEAVE_REMARK)
                    ));
                } else {
                    JSONObject jsonObject = jsonArray.getJSONObject (i);
                    if (jsonObject.getInt (AppConfigTags.TYPE_ID) == type_id) {
                        leaveList.add (new Leave (
                                jsonObject.getInt (AppConfigTags.REQUEST_ID),
                                jsonObject.getInt (AppConfigTags.TYPE_ID),
                                jsonObject.getInt (AppConfigTags.LEAVE_STATUS),
                                jsonObject.getDouble (AppConfigTags.LEAVE_AVAILED),
                                jsonObject.getString (AppConfigTags.LEAVE_TYPE),
                                jsonObject.getString (AppConfigTags.LEAVE_FROM),
                                jsonObject.getString (AppConfigTags.LEAVE_TILL),
                                jsonObject.getString (AppConfigTags.LEAVE_DESCRIPTION),
                                jsonObject.getString (AppConfigTags.LEAVE_CREATED_AT),
                                jsonObject.getString (AppConfigTags.LEAVE_UPDATED_AT),
                                jsonObject.getString (AppConfigTags.LEAVE_UPDATED_BY),
                                jsonObject.getString (AppConfigTags.LEAVE_REMARK)
                        ));
                    }
                }
                
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        
        
        leaveAdapter = new LeaveAdapter (getActivity (), leaveList);
        leaveAdapter.SetOnItemClickListener (new LeaveAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
            }
        });
        
        rvLeaves.setAdapter (leaveAdapter);
        rvLeaves.setHasFixedSize (true);
        rvLeaves.setLayoutManager (linearLayoutManager);
        rvLeaves.setItemAnimator (new DefaultItemAnimator ());
        
        rvLeaves.addItemDecoration (
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
                    LeaveAdapter leaveAdapter = new LeaveAdapter (getActivity (), leaveList);
                    rvLeaves.setAdapter (leaveAdapter);
                    leaveAdapter.SetOnItemClickListener (new LeaveAdapter.OnItemClickListener () {
                        @Override
                        public void onItemClick (View view, int position) {
                        }
                    });
                    
                    rlNoResultFound.setVisibility (View.GONE);
                    rvLeaves.setVisibility (View.VISIBLE);
                }
                if (s.toString ().length () > 0) {
                    leaveListTemp.clear ();
                    for (Leave leave : leaveList) {
                        if (leave.getType_name ().toUpperCase ().contains (s.toString ().toUpperCase ()) ||
                                leave.getType_name ().toLowerCase ().contains (s.toString ().toLowerCase ())) {
                            leaveListTemp.add (leave);
                        }
                    }
                    leaveAdapter = new LeaveAdapter (getActivity (), leaveListTemp);
                    rvLeaves.setAdapter (leaveAdapter);
                    leaveAdapter.SetOnItemClickListener (new LeaveAdapter.OnItemClickListener () {
                        @Override
                        public void onItemClick (View view, int position) {
                        }
                    });
                    
                    if (leaveListTemp.size () == 0) {
                        rlNoResultFound.setVisibility (View.VISIBLE);
                        rvLeaves.setVisibility (View.GONE);
                    } else {
                        rvLeaves.setVisibility (View.VISIBLE);
                        rlNoResultFound.setVisibility (View.GONE);
                    }
                }
            }
        });
    }
    
    
    public void setOnDialogResultListener (OnDialogResultListener listener) {
        this.onDialogResultListener = listener;
    }
    
    @Override
    public void onDismiss (DialogInterface dialog) {
        super.onDismiss (dialog);
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