package com.example.haya.im.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.haya.im.R;
import com.example.haya.im.bean.User;

import java.util.ArrayList;

public class AdapterResult extends RecyclerView.Adapter<AdapterResult.ViewHolder> {

    private ArrayList<User> list;

    public AdapterResult(ArrayList<User> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterResult.ViewHolder holder, int position) {
        User obj = list.get(position);

        holder.name.setText(obj.getName());
    }

    @NonNull
    @Override
    public AdapterResult.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_item, parent, false);
        return new AdapterResult.ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.res_name);
        }
    }
}
