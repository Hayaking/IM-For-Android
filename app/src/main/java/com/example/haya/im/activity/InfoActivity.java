package com.example.haya.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.haya.im.R;

public class InfoActivity extends AppCompatActivity {
    private String name;

    public void call(View view) {
    }

    public void msg(View view) {
        Intent intent = new Intent(InfoActivity.this, MessageActivity.class);
        intent.putExtra("toName", name);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        TextView tv_name = findViewById(R.id.info_name);
        tv_name.setText(name);
    }

}
