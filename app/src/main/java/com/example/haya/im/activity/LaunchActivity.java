package com.example.haya.im.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.haya.im.R;
import com.ninetripods.aopermission.permissionlib.annotation.NeedPermission;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends AppCompatActivity {

    private static boolean mBackKeyPressed = false;//记录是否有首次按键

    @NeedPermission(value = {Manifest.permission.INTERNET})
    public void getPms() {

    }

    @Override
    public void onBackPressed() {
        if (!mBackKeyPressed) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {//退出程序
            this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //后台处理耗时任务
        new Thread(() -> {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

            //耗时任务，比如加载网络数据
            runOnUiThread(() -> {
                //跳转至 MainActivity
                Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                startActivity(intent);
                //结束当前的 Activity
                LaunchActivity.this.finish();
            });
        }).start();

    }
}
