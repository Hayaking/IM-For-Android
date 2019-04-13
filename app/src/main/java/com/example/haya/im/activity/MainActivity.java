package com.example.haya.im.activity;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.haya.im.IMService;
import com.example.haya.im.R;
import com.example.haya.im.client.Client;
import com.example.haya.im.client.Message;
import com.example.haya.im.fragment.About;
import com.example.haya.im.fragment.Contacts;
import com.example.haya.im.fragment.History;
import com.example.haya.im.utils.MySQLite;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private static boolean mBackKeyPressed = false;//记录是否有首次按键
    private FragmentManager fragmentManager;
    private IMService imService;
    private IMService.EchoServiceBinder iBinder;
    private Toolbar toolbar;
    private SearchView mSearchView;
    private MenuItem searchItem;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_history:
//                toolbar.setVisibility(View.VISIBLE);

                toFgHistory();
                return true;
            case R.id.navigation_contacts:
//                toolbar.setVisibility(View.VISIBLE);

                toFgContacts();
                return true;
            case R.id.navigation_about:
                toFgAbout();
//                toolbar.setVisibility(View.GONE);

                return true;
        }
        return false;
    };

    private void initView() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentManager = getFragmentManager();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        Drawable navigationIcon = toolbar.getNavigationIcon();
        navigationIcon.setTint(Color.rgb(255, 255, 255));
        toolbar.inflateMenu(R.menu.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {

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
        } else {
            //退出程序
            this.finish();
//            System.exit(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (LoginActivity.self != null)
            LoginActivity.self.finish();
        MySQLite.getInstance(MainActivity.this);
        Intent intent = new Intent(MainActivity.this, IMService.class);

        bindService(intent, this, Context.BIND_AUTO_CREATE);
        initView();
        toFgHistory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        searchItem = menu.findItem(R.id.item_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnSearchClickListener(v -> {
            Toast.makeText(MainActivity.this, "os", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            startActivity(intent);
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Client instance = Client.getInstance();
        instance.send(Message.TYPE.OFFLINE, null);
//        instance.sendOffline();
        unbindService(this);
        Log.println(Log.INFO, "销毁", "!!!!!!!!");
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {//mActionBarToolbar就是android.support.v7.widget.Toolbar
            toolbar.setTitle(null);//设置为空，可以自己定义一个居中的控件，当做标题控件使用
        }
    }

    @Override
    protected void onResume() {
        if (mSearchView != null) {
            //折叠searView
            mSearchView.onActionViewCollapsed();
        }
        super.onResume();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        iBinder = ((IMService.EchoServiceBinder) service);
        //获得Service实例
        imService = iBinder.getService();
        imService.receive();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private void toFgAbout() {
        About about = About.newInstance();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fg_container, about)
                .commitAllowingStateLoss();
    }

    private void toFgContacts() {
        Contacts contacts = Contacts.newInstance(iBinder);
        fragmentManager
                .beginTransaction()
                .replace(R.id.fg_container, contacts)
                .commitAllowingStateLoss();
    }

    private void toFgHistory() {
        History history = History.newInstance("参数");
        fragmentManager
                .beginTransaction()
                .replace(R.id.fg_container, history)
                .commitAllowingStateLoss();
    }
}
