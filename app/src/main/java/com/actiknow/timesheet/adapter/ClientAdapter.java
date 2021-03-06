package com.actiknow.timesheet.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.Client;
import com.actiknow.timesheet.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {
    private OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Client> clientList = new ArrayList<> ();
    
    public ClientAdapter (Activity activity, List<Client> clientList) {
        this.activity = activity;
        this.clientList = clientList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_client, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Client client = clientList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvName);
        holder.tvName.setText (client.getName ());
    }
    
    @Override
    public int getItemCount () {
        return clientList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        
        public ViewHolder (View view) {
            super (view);
            tvName = (TextView) view.findViewById (R.id.tvName);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}