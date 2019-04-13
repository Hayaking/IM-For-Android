package com.example.haya.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haya.im.R;
import com.example.haya.im.adapter.AdapterResult;
import com.example.haya.im.bean.User;
import com.example.haya.im.client.Client;
import com.example.haya.im.client.Message;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    public static ViewHolder viewHolder;
    private SearchView mSearchView;

    public void add(View view) {
        Toast.makeText(ResultActivity.this, "add", Toast.LENGTH_SHORT).show();
        TextView name = findViewById(R.id.res_name);
        Client instance = Client.getInstance();
//        instance.add(name.getText().toString().trim());
        instance.send(Message.TYPE.ADD, name.getText().toString().trim());
        Intent intent = new Intent();
        intent.putExtra("res", "res");
        setResult(1, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        viewHolder = new ViewHolder();
        mSearchView = findViewById(R.id.res_searchView);
        //自动展开
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("tag", newText);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Client instance = Client.getInstance();
                instance.send(Message.TYPE.GET_USER, query);
//                instance.queryUser(query);
                return false;
            }
        });
    }

    public class ViewHolder {
        private TextView text;
        private SwipeMenuRecyclerView listView;
        private AdapterResult adapter;
        private Handler handler = new Handler();
        private User user = null;
        private ArrayList<User> result = new ArrayList<>();
        private Runnable updateUI = new Runnable() {
            @Override
            public void run() {
                text.setVisibility(View.GONE);
                result.clear();
                result.add(user);
                adapter.notifyDataSetChanged();
                user = null;
            }
        };
        private Runnable nullUpdate = new Runnable() {
            @Override
            public void run() {
                text.setVisibility(View.VISIBLE);
                text.setText("啥都没找到");
                result.clear();

                adapter.notifyDataSetChanged();
                user = null;
            }
        };

        public ViewHolder() {
            text = findViewById(R.id.res_title);
            listView = findViewById(R.id.res_result);
            listView.setLayoutManager(new LinearLayoutManager(ResultActivity.this));
            adapter = new AdapterResult(result);
            listView.setAdapter(adapter);
        }

        public void setUser(User user) {
            this.user = user;
            if (null != user)
                handler.post(updateUI);
            else
                handler.post(nullUpdate);
        }
    }
}
