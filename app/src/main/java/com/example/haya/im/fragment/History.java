package com.example.haya.im.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.haya.im.R;
import com.example.haya.im.activity.AcceptActivity;
import com.example.haya.im.activity.MessageActivity;
import com.example.haya.im.adapter.AdapterHistory;
import com.example.haya.im.bean.Historical;
import com.example.haya.im.client.Client;
import com.example.haya.im.utils.DBUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;

public class History extends Fragment {

    public static History newInstance(String args) {
        Bundle bundle = new Bundle();
        bundle.putString("args", args);
        History fragment = new History();
        fragment.setArguments(bundle);
        return fragment;
    }
    public static ViewHolder viewHolder;

    public History() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getContext();
        viewHolder = new ViewHolder(context);

    }


    public class ViewHolder {
        private AdapterHistory adapter;
        private ArrayList<Historical> history;
        private SwipeMenuRecyclerView listView;
        private Handler handler = new Handler();
        private Runnable updateUI = new Runnable() {
            @Override
            public void run() {
                history.clear();
                history.addAll(DBUtils.getHistory());
                history.add(new Historical(Client.getInstance().getAccount(), "复读机", Historical.TYPE.SELF));


                adapter.notifyDataSetChanged();
            }
        };

        public ViewHolder(Context context) {
            listView = getView().findViewById(R.id.ls_history);
            listView.setLayoutManager(new LinearLayoutManager(context));
            history = DBUtils.getHistory();
            adapter = new AdapterHistory(history);
            listView.setSwipeMenuCreator((swipeLeftMenu, swipeRightMenu, viewType) -> {
                int size = 180;
                SwipeMenuItem deleteItem = new SwipeMenuItem(context)
                        .setBackgroundColor(Color.RED)
                        .setText("删除") // 文字。
                        .setTextColor(Color.BLACK) // 文字颜色。
                        .setTextSize(16) // 文字大小。
                        .setWidth(size)
                        .setHeight(size);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。.

            });
            //设置侧滑菜单的点击事件
            listView.setSwipeMenuItemClickListener(menuBridge -> {
                menuBridge.closeMenu();
//            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。0是左，右是1，暂时没有用到
                int position = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
//            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                DBUtils.deleteHistory(history.get(position));
                history.remove(position);
                Toast.makeText(context, "删除" + position, Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            });
            //跳转到发送信息的页面
            listView.setSwipeItemClickListener((itemView, position) -> {
                Intent intent = null;
                switch (history.get(position).getType()) {
                    case NORMAL:
                        intent = new Intent(context, MessageActivity.class);
                        intent.putExtra("toName", history.get(position).getName());
                        break;
                    case NOTICE_ADD:
                        intent = new Intent(context, AcceptActivity.class);
                        break;
                    case SELF:
                        intent = new Intent(context, MessageActivity.class);
                        intent.putExtra("toName", history.get(position).getName());
                        break;
                }
                String name = history.get(position).getName();
                intent.putExtra("toName", name);
                DBUtils.updateMessageFlag(name, false);
                setNewItem();
                startActivity(intent);
            });
            listView.setAdapter(adapter);
        }

        public void setNewItem() {
            handler.post(updateUI);
        }
    }

}
