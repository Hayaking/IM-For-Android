package com.example.haya.im.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haya.im.R;
import com.example.haya.im.bean.Historical;
import com.example.haya.im.client.Client;

import java.util.ArrayList;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {
    private ArrayList<Historical> list;

    public AdapterHistory(ArrayList<Historical> list) {
        list.add(new Historical(Client.getInstance().getAccount(), "复读机", Historical.TYPE.SELF));
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Historical obj = list.get(position);
        switch (obj.getType()) {
            case SELF:
                holder.avatar.setImageResource(R.drawable.self);
                holder.text.setText(obj.getText());
                break;
            case NOTICE:
                holder.avatar.setImageResource(R.drawable.notice);
                holder.text.setText(obj.getText());
                break;
            case NORMAL:
                holder.avatar.setImageResource(R.drawable.avatar);
                holder.text.setText(obj.getText());
                break;
            case NOTICE_ADD:
                holder.avatar.setImageResource(R.drawable.add);
                holder.text.setText("请求添加好友");
                break;
            case NOTICE_ACCRPTED:
                holder.avatar.setImageResource(R.drawable.accepted);
                holder.text.setText(obj.getText());
                break;
        }
        holder.name.setText(obj.getName());
//        holder.reddot.setVisibility(obj.isFlag()?View.VISIBLE:View.GONE);
//        if (obj.getType() == Historical.TYPE.NOTICE)
//            holder.avatar.setImageResource(R.drawable.notice);
//        else
//            holder.avatar.setImageResource(R.drawable.avatar);

        if (obj.isFlag()) {
            holder.reddot.setVisibility(View.VISIBLE);
        } else {
            holder.reddot.setVisibility(View.GONE);
        }
        obj.setFlag(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView text;
        TextView reddot;
        ImageView avatar;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.his_name);
            text = itemView.findViewById(R.id.his_container);
            reddot = itemView.findViewById(R.id.red_dot);
            avatar = itemView.findViewById(R.id.his_avatar);
        }
    }
}
