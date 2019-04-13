package com.example.haya.im.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haya.im.R;
import com.example.haya.im.client.Message;

import java.util.List;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.ViewHolder> {
    private List<Message> mMsgList;

    public AdapterMessage(List<Message> mMsgList) {
        this.mMsgList = mMsgList;
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Message msg = mMsgList.get(position);
        Message.TYPE type = msg.getType();
        switch (type) {
            case RECEIVED_SELF:
            case RECEIVED:
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftMsg.setText(msg.getText());
                break;
            case SEND_SELF:
            case SEND:
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightMsg.setText(msg.getText());
                break;
        }
        //增加对消息类的判断，如果这条消息是收到的，显示左边布局，是发出的，显示右边布局
//        if (type == Message.TYPE.RECEIVED || ) {
//
//        } else if (type == Message.TYPE.SEND) {
//
//        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view) {
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftMsg = view.findViewById(R.id.left_msg);
            rightMsg = view.findViewById(R.id.right_msg);
        }
    }

}
