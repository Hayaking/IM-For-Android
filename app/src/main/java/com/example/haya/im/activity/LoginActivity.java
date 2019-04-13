package com.example.haya.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.haya.im.R;
import com.example.haya.im.client.Client;
import com.example.haya.im.client.Message;
import com.example.haya.im.utils.GetPublicIP;
import com.example.haya.im.utils.MySQLite;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {
    private static boolean mBackKeyPressed = false;//记录是否有首次按键
    public static LoginActivity self;
    private EditText et_account;
    private EditText et_psw;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        self = this;
        init();
        client = Client.getInstance();
        //获得公钥
        client.send(Message.TYPE.GET_RSA_PUBLICKEY);
        client.get();
    }

    private void init() {
        et_account = findViewById(R.id.et_account);
        et_psw = findViewById(R.id.et_password);
        ToggleButton tg_visual = findViewById(R.id.tg_visual);
        ToggleButton tg_fork = findViewById(R.id.tg_fork);
        Button bt_sign = findViewById(R.id.bt_sign);
        et_psw.setOnFocusChangeListener((v, hasFocus) -> tg_visual.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE));
        et_account.setOnFocusChangeListener((v, hasFocus) -> tg_fork.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE));
        bt_sign.setOnClickListener(v -> sign());
        //清空帐号
        tg_fork.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                et_account.getText().clear();
            }
        });
        //密码显隐
        tg_visual.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tg_visual.setChecked(isChecked);
            tg_visual.setBackgroundResource(isChecked ? R.drawable.visual : R.drawable.invisible);
            et_psw.setTransformationMethod(
                    isChecked
                            ? HideReturnsTransformationMethod.getInstance()
                            : PasswordTransformationMethod.getInstance()
            );
        });
        MySQLite.getInstance(LoginActivity.this);
    }

    public void sign() {
        String account = et_account.getText().toString().trim();
        String psw = et_psw.getText().toString().trim();
//        Client client = Client.getInstance();
        client.setAccount(account, psw);
        client.send(Message.TYPE.SIGN, null);
        boolean flag = client.get();
        if (flag) {
            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
        }
    }

    public void login(View view) {
        String account = et_account.getText().toString().trim();
        String psw = et_psw.getText().toString().trim();
//        Client client = Client.getInstance();
        client.setAccount(account, psw);
        client.send(Message.TYPE.LOGIN, null);
        boolean flag = client.get();
        if (flag) {
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
        }
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
}

