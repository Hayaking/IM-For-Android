package com.example.haya.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.haya.im.R;
import com.example.haya.im.client.Client;
import com.example.haya.im.client.Message;

public class AcceptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        Intent intent = getIntent();
        String toName = intent.getStringExtra("toName");
        Button button = findViewById(R.id.bt_ac);
        button.setOnClickListener(v -> {
            Client instance = Client.getInstance();
            instance.send(Message.TYPE.ACCEPT, toName);
//            instance.accept(toName);
        });
    }
}
