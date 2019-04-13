package com.example.haya.im.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haya.im.R;
import com.example.haya.im.adapter.AdapterMessage;
import com.example.haya.im.client.Client;
import com.example.haya.im.client.Message;
import com.example.haya.im.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    public static ViewHolder viewHolder;
    private String toName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toName = getIntent().getStringExtra("toName");
        viewHolder = new ViewHolder(DBUtils.getMessage(toName));
    }

    /**
     * 发送消息
     *
     * @param view
     */
    public void send(View view) {
        Client client = Client.getInstance();
        String text = viewHolder.editText.getText().toString().trim();
        if ("".equals(text)) {
            Toast.makeText(MessageActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Message msg;
            if (toName.equals(client.getAccount())) {
                msg = client.send(Message.TYPE.SEND_SELF, text,toName);
            } else {
                msg = client.send(Message.TYPE.SEND, text, toName);
            }
            viewHolder.list.add(msg);
            viewHolder.adapter.notifyDataSetChanged();
            viewHolder.editText.getText().clear();
            viewHolder.recyclerView.smoothScrollToPosition(viewHolder.list.size());
        }
    }

    public class ViewHolder {
        private List<Message> list;
        private AdapterMessage adapter;
        private RecyclerView recyclerView;
        private EditText editText;
        private Handler handler = new Handler();
        private Runnable updateAd = new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(list.size());
                adapter.notifyDataSetChanged();
            }
        };

        public ViewHolder(ArrayList<Message> message) {
            list = message;
            editText = findViewById(R.id.msg_ed);
//            msgEt.setOnFocusChangeListener((v, hasFocus) -> Toast.makeText(MessageActivity.this, "点击了编辑框", Toast.LENGTH_SHORT).show());
            recyclerView = findViewById(R.id.message_recylerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
            adapter = new AdapterMessage(list);
            recyclerView.setAdapter(adapter);
        }

        public void setrMsg(Message message) {
            if (message != null) {
                list.add(message);
                handler.post(updateAd);
            }
        }


    }
}
