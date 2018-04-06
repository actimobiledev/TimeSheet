package com.actiknow.timesheet.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actiknow.timesheet.R;
import com.actiknow.timesheet.model.Clients;

import java.util.ArrayList;
import java.util.List;


public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;

    private Activity activity;
    private List<Clients> clientList = new ArrayList<>();

    public ClientsAdapter(Activity activity, List<Clients> clientList) {
        this.activity = activity;
        this.clientList = clientList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.list_item_clients, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);

        final Clients client = clientList.get(position);

        holder.tvName.setText(client.getName());
        holder.tvDescription.setText(client.getCompany());
      //  holder.tvTime.setText(client.getHours());
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvDescription;
        TextView tvTime;

        ProgressBar progressBar;


        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById (R.id.tvName);
            tvDescription = (TextView) view.findViewById (R.id.tvDescription);
            tvTime = (TextView) view.findViewById (R.id.tvTime);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }
    }
    
    
}


